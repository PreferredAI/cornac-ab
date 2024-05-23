package ai.preferred.cornac.service;

import ai.preferred.cornac.config.CornacProperties;
import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.CornacInstanceRepository;
import ai.preferred.cornac.repository.DemoUserRepository;
import ai.preferred.cornac.repository.ExperimentRepository;
import ai.preferred.cornac.repository.UserAbAllocationRepository;
import ai.preferred.cornac.util.FileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.logging.LogLevel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CornacService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CornacService.class);
    private final List<CornacInstance> inMemoryCornacInstances = new ArrayList<>();

    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private DemoUserRepository demoUserRepository;
    @Autowired
    private UserAbAllocationRepository userAbAllocationRepository;

    @Autowired
    private CornacProperties cornacProperties;

    @Transactional
    public CornacInstanceDto createCornacInstance(String name, Integer experimentId, MultipartFile file) {

        // Do OS Checks
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        if (isWindows){
            System.out.println("Windows is currently not supported. " +
                    "Please use the docker version. More information in readme.");
            System.exit(0);
        }

        // Get Experiment details
        Experiment experiment = experimentRepository.findById(experimentId).get();

        // Check if file attached is a zip file
        if (!FileUtil.isFileExtension(file, ".zip")){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "invalid file type attached. only '.zip' files are accepted."
            );
        }

//        storeFileInTemp(file, name);
//        unzipFile(cornacProperties.getUploadDir() + "/" + name + "/file.zip", "uploads/" + name + "/output/", true);

        return startCornacInstance(name, experiment, false);
    }

    @Transactional
    public CornacInstanceDto startCornacInstance(String name, Experiment experiment, boolean isRestart) {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        int port;

        try (ServerSocket serverSocket = new ServerSocket(0)){
            port = serverSocket.getLocalPort(); // get a random available port
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String directory = cornacProperties.getUploadDir()
                + "/" + name;

        // unzip file
        unzipFile(directory + "/file.zip", directory + "/output/", true);
        directory = directory + "/output/";

        // find directory with pkl files (in case if the directory is nested)
        directory = findDirectoryWithExt(directory, ".pkl");

        // check if .meta file exist
        checkIfFileWithExtExists(directory, ".meta");

        Map<String, String> metaMap = getMetadataMap(directory);

        String modelClass = "cornac.models." + metaMap.get("model_classname");


        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            // windows cmd file is not yet included.
            builder.command("cmd.exe", "cornac-run.cmd", Integer.toString(port), directory, modelClass);
        } else {
            builder.command("sh", cornacProperties.getExecutableDir() + "/cornac-run.sh", Integer.toString(port), directory, modelClass);
        }

        try {
            Process process = builder.start();
            LOGGER.info("Starting service '{}' ({}) class: '{}', at port: {}", name, directory, modelClass, port);

            if (process.waitFor(10, TimeUnit.SECONDS)){
                LOGGER.info("Service failed to start.");
                List<String> errors = readOutput(process.getErrorStream());
                errors.forEach(error -> LOGGER.error("error output> " + error));
//                List<String> outputs = readOutput(process.getInputStream());
//                outputs.forEach(output -> LOGGER.info("output> " + output));

                throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                        new RuntimeException("Unable to start service. Please try again.")
                );
            } else {
                LOGGER.info("Service started at port: {}", port);

                CornacInstance cornacInstance = addCornacInstance(name, modelClass, port, experiment, process, isRestart);
                return new CornacInstanceDto(cornacInstance.getServiceName(), cornacInstance.getPort(), "Running" );
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getMetadataMap(String directory) {
        // read meta file
        String metaFile = getFileWithExt(directory, ".meta");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(metaFile))){
//            List<String> metaLines = reader.lines().collect(Collectors.toList());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readValue(reader, JsonNode.class);

            return mapper.convertValue(jsonNode, new TypeReference<>() {}); // convert to hashmap

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unzipFile(String fileDir, String outputFolder, boolean ignoreFolders) throws ResponseStatusException {
        try {
            FileUtil.unzipFile(fileDir, outputFolder, ignoreFolders);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to unzip file. Please try again."
            );
        }
    }

    private String getFileWithExt(String directory, String ext) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            Optional<Path> path = paths
                    .filter(Files::isReadable)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(ext))
                    .findFirst();
            if (path.isPresent()){
                return path.get().toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                new RuntimeException("File with extension " + ext + " not found in directory.")
        );
    }

    private String findDirectoryWithExt(String directory, String ext) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            Optional<Path> path = paths.filter(p -> p.toString().endsWith(ext)).findFirst();
            if (path.isPresent()){
                return path.get().getParent().toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                new RuntimeException("File with extension " + ext + " not found in directory.")
        );
    }

    private void checkIfFileWithExtExists(String directory, String ext) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            if (paths.anyMatch(path -> path.toString().endsWith(ext))){
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                new RuntimeException("File with extension " + ext + " not found in directory.")
        );
    }

    private List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    public List<CornacInstance> getCornacInstancesForExperiment(Integer experimentId) {
        return cornacInstanceRepository.findCornacInstanceByExperimentId(experimentId);
    }

    public List<CornacInstanceDto> getCornacInstanceDtosForExperiment(Integer experimentId) {
        List<CornacInstance> cornacInstances = cornacInstanceRepository.findCornacInstanceByExperimentId(experimentId);
        return cornacInstances.stream()
                .map(cornacInstance -> convertToCornacInstanceDto(cornacInstance))
                .toList();
    }

    public List<CornacInstance> getInMemoryCornacInstances() {
        return inMemoryCornacInstances;
    }

//    public List<CornacInstanceDto> getCornacInstances() {
//        return activeCornacInstances.stream()
//                .map(activeCornacInstance -> convertToCornacInstanceDto(activeCornacInstance))
//                .toList();
//    }

    private CornacInstanceDto convertToCornacInstanceDto(CornacInstance cornacInstance){
        CornacInstanceDto cornacInstanceDto = modelMapper.map(cornacInstance, CornacInstanceDto.class);
        cornacInstanceDto.setStatus("Running");
        return cornacInstanceDto;
    }

//    private CornacInstance updateCornacInstance(String name, String modelClass, Integer port, Experiment experiment, Process process) {
//        WebClient webClient = WebClient.create(
//                String.format("http://localhost:%d/", port)
//        );
//
//        CornacInstance cornacInstance = cornacInstanceRepository.findCornacInstanceByServiceNameAndModelClassAndExperimentId(name, modelClass, experiment.getId());
//        cornacInstance.setProcess(process);
//        cornacInstance.setPort(port);
//        cornacInstance.setWebClient(webClient);
//
//        cornacInstance = cornacInstanceRepository.save(cornacInstance);
//
//        inMemoryCornacInstances.add(cornacInstance);
//        return cornacInstance;
//    }

    @Transactional
    public CornacInstance addCornacInstance(String name, String modelClass, Integer port, Experiment experiment, Process process, boolean isRestart) {
//        WebClient webClient = WebClient.create(
//                String.format("http://localhost:%d/", port)
//        );

//        HttpClient httpClient = HttpClient.create().wiretap(true);
//        ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);
//
//        WebClient webClient = WebClient.builder()
//                .baseUrl(String.format("http://localhost:%d/", port))
//                .clientConnector(conn)
//                .build();

        final int size = 16 * 1024 * 1024; // increases the max in-memory size to 16MB
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        WebClient webClient = WebClient.builder()
                .baseUrl(String.format("http://localhost:%d/", port))
                .exchangeStrategies(strategies)
                .build();

        CornacInstance cornacInstance;
        if (isRestart){
            cornacInstance = cornacInstanceRepository.findCornacInstanceByServiceNameAndModelClassAndExperimentId(name, modelClass, experiment.getId());
            cornacInstance.setProcess(process);
            cornacInstance.setPort(port);
            cornacInstance.setWebClient(webClient);
            cornacInstance.setStatus("running");
        } else {
            cornacInstance = new CornacInstance(name, modelClass, port, experiment, process, webClient);
        }

        cornacInstanceRepository.save(cornacInstance);
        inMemoryCornacInstances.add(cornacInstance);
        return cornacInstance;
    }

    @Transactional
    public boolean allocateUsersToInstances(Experiment experiment) {
        List<CornacInstance> cornacInstances = getCornacInstancesForExperiment(experiment.getId());
        if (cornacInstances.isEmpty()){
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                    new RuntimeException("No cornac instances found for experiment.")
            );
        }

        List<DemoUser> users = getUsers();

        if (users.isEmpty()){
            experiment.setUserIndexFound(false);
            experimentRepository.save(experiment);
        }

        List<UserAbAllocation> userAbAllocations = new ArrayList<>();

        Long randomSeed = experiment.getUserSeed();
        int numInstances = cornacInstances.size();

        for (DemoUser user : users) {
            String userId = user.getUserId();

            UserAbAllocation userAbAllocation = new UserAbAllocation();
            userAbAllocation.setUserId(userId);
            userAbAllocation.setAbGroup(getAbInstanceGroup(numInstances, randomSeed, userId));
            userAbAllocation.setExperiment(experiment);

            userAbAllocations.add(userAbAllocation);
        }

        userAbAllocationRepository.saveAll(userAbAllocations);

        return true;
    }

    public List<DemoUser> getUsers(){
        List<DemoUser> users = new ArrayList<>();
        try (Stream<DemoUser> userStream = demoUserRepository.findAllBy()){
            users.addAll(userStream.toList());
        }
        return users;
    }

    public boolean isInstanceStillAlive (CornacInstance cornacInstance) {
        if (!inMemoryCornacInstances.contains(cornacInstance)){
            return false;
        }

        CornacInstance localCornacInstance = inMemoryCornacInstances.get(inMemoryCornacInstances.indexOf(cornacInstance));
        if (localCornacInstance.getProcess() == null){
            return false;
        }

        return true;
    }

    public boolean removeInMemoryCornacInstance(CornacInstance cornacInstance) {
        return inMemoryCornacInstances.remove(cornacInstance);
    }

    private void storeFileInTemp(MultipartFile file, String dirName) {
        if (file.isEmpty()){
            throw new RuntimeException("Empty file attached. Unable to store.");
        }

        Path uploadDir = Paths.get(cornacProperties.getUploadDir());

        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(uploadDir.resolve(dirName));
            Files.copy(inputStream, uploadDir.resolve(dirName + "/file.zip"), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Recommendation getApiRecommendation(CornacInstance cornacInstance, String userId, String k) {
        if (cornacInstance == null){
            throw new RuntimeException("Cornac instance not provided. Unable to get recommendation.");
        }
        WebClient webClient = cornacInstance.getWebClient();
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("recommend")
                        .queryParam("uid", userId)
                        .queryParamIfPresent("k", Optional.ofNullable(k))
                        .build())
                .retrieve()
                .bodyToMono(Recommendation.class)
//                .onErrorMap(throwable -> new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("Unable to get recommendation")))
                .block();
    }

    public CornacEvaluationResponse postCornacInstanceEvaluation(CornacInstance cornacInstance, CornacEvaluationRequest evaluationRequest) {
//        WebClient webClient = WebClient.create(
//                String.format("http://localhost:%d/", 59330)
//        );
//        CornacEvaluationRequest cornacEvaluationRequest = new CornacEvaluationRequest();
//        cornacEvaluationRequest.setMetrics(Arrays.asList("RMSE()", "NDCG(k=10)"));
//        List<List<Object>> data = new ArrayList<>();
//        data.add(Arrays.asList("123", "1539", 1));
//        data.add(Arrays.asList("123", "2", 1));
//        data.add(Arrays.asList("124", "1", 1));
//        cornacEvaluationRequest.setData(data);

        return cornacInstance.getWebClient()
                .post()
                .uri("evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(evaluationRequest))
                .retrieve()
                .bodyToMono(CornacEvaluationResponse.class)
                .doOnError(error -> {
                    error.printStackTrace();
                })
                .block();
    }

    private Integer getAbInstanceGroup(Integer numInstances, Long randomSeed, String userId){
        // allocate an ab-testing group
        // - abGroup is a random number between 0 and numInstances
        // - this is a deterministic random operation.
        // - This will give the same instance on the same seed for the same user.
        Random rand = new Random(randomSeed);
        int randInt = rand.nextInt(userId.hashCode());
        return randInt % numInstances;
    }

    public boolean updateCornacInstanceStatus(Integer id, String status) {
        CornacInstance cornacInstance = cornacInstanceRepository.findById(id).get();
        cornacInstance.setStatus(status);
        cornacInstanceRepository.save(cornacInstance);
        return true;
    }




}
