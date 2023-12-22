package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.RecommendLogRepository;
import ai.preferred.cornac.util.FileUtil;
import jakarta.annotation.PreDestroy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendService.class);
    private final List<CornacInstance> activeCornacInstances = new ArrayList<>();

    @Autowired
    private RecommendLogRepository recommendLogRepository;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private ModelMapper modelMapper;

    public CornacInstanceDto createCornacInstance(String name, String modelClass, MultipartFile file) {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        if (isWindows){
            System.out.println("Windows is currently not supported. " +
                    "Please use the docker version. More information in readme.");
            System.exit(0);
        }

        if (!FileUtil.isFileExtension(file, ".zip")){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "invalid file type attached. only '.zip' files are accepted."
            );
        }

        storeFileInTemp(file, modelClass + "-" + name);
        try {
            FileUtil.unzipFile("uploads/" + modelClass + "-" + name + "/file.zip", "uploads/" + modelClass + "-" + name + "/output/", true);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to unzip file. Please try again."
            );
        }

        String modelFolder = "uploads/" + modelClass + "-" + name + "/output/";

        Integer port = 5000;

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            // windows cmd file is not yet included.
            builder.command("cmd.exe", "cornac-run.cmd", port.toString(), modelFolder, modelClass);
        } else {
            builder.command("sh", "cornac-run.sh", port.toString(), modelFolder, modelClass);
        }

        try {
            Process process = builder.start();
            addCornacInstance(name, port, process);

            LOGGER.info("Starting service at port: {}", port);

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
                return new CornacInstanceDto(name, port, "created");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    public List<CornacInstanceDto> getCornacInstances() {
        return activeCornacInstances.stream()
                .map(activeCornacInstance -> convertToCornacInstanceDto(activeCornacInstance))
                .toList();
    }

    private CornacInstanceDto convertToCornacInstanceDto(CornacInstance cornacInstance){
        CornacInstanceDto cornacInstanceDto = modelMapper.map(cornacInstance, CornacInstanceDto.class);
        cornacInstanceDto.setStatus("created");
        return cornacInstanceDto;
    }

    public void addCornacInstance(String name, int port, Process process) {
        WebClient webClient = WebClient.create(
                String.format("http://localhost:%d/", port)
        );
        activeCornacInstances.add(new CornacInstance(name, port, process, webClient));
    }

    private void storeFileInTemp(MultipartFile file, String dirName) {
        if (file.isEmpty()){
            throw new RuntimeException("Empty file attached. Unable to store.");
        }

        Path uploadDir = Paths.get("uploads");

        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(uploadDir.resolve(dirName));
            Files.copy(inputStream, uploadDir.resolve(dirName + "/file.zip"), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public RecommendLogDto getRecommendations(String userId, String k){
        int numInstances = activeCornacInstances.size();

        Experiment experiment = experimentService.getCurrentExperiment();
        ExperimentType experimentType = experiment.getType();

        int userIdHash = Math.abs(userId.hashCode());

        int chosenInstance;

        switch (experimentType){
            case TIME:
                int hour = LocalDateTime.now().getHour();
                int interval = hour % experiment.getTimeHoursSwitch();
                chosenInstance = interval % numInstances;
                break;
            case USER:
                // run a deterministic random operation.
                // - This will give the same instance on every run for the same user.
                Random rand = new Random(experiment.getUserSeed());
                int randInt = rand.nextInt(userIdHash);
                chosenInstance = randInt % numInstances;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid 'type': " +
                        "Please indicate 'time' for time-sliced and 'user' for user-sliced A-B Testing.");
        }

        CornacInstance cornacInstance = activeCornacInstances.get(chosenInstance);
        WebClient webClient = cornacInstance.getWebClient();
        Recommendation recommendation = getApiRecommendation(webClient, userId, k);

        // add recommendation to log
        RecommendLog log = new RecommendLog(
                null,
                recommendation.getRecommendations(),
                recommendation.getQuery(),
                new ArrayList<>()
        );

        RecommendLog recommendLog = recommendLogRepository.save(log);
        return convertToRecommendLogDto(recommendLog);
    }

    public Recommendation getApiRecommendation(WebClient webClient, String userId, String k) {


        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("recommend")
                    .queryParam("uid", userId)
                    .queryParamIfPresent("k", Optional.ofNullable(k))
                    .build())
                .retrieve()
                .bodyToMono(Recommendation.class)
                .block();
    }

    private RecommendLogDto convertToRecommendLogDto(RecommendLog recommendLog) {
        return modelMapper.map(recommendLog, RecommendLogDto.class);
    }



    @PreDestroy
    public void destroy(){
        System.out.println("destroy called, closing all cornac instances.");
        activeCornacInstances.forEach(cornacInstance -> cornacInstance.getProcess().destroy());
    }


}
