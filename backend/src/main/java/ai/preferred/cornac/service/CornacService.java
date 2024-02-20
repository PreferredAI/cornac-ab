package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.entity.CornacInstance;
import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.repository.CornacInstanceRepository;
import ai.preferred.cornac.repository.ExperimentRepository;
import ai.preferred.cornac.util.FileUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CornacService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CornacService.class);
    private final List<CornacInstance> activeCornacInstances = new ArrayList<>();

    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ExperimentRepository experimentRepository;

    public CornacInstanceDto createCornacInstance(String name, String modelClass, Long experimentId, MultipartFile file) {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        if (isWindows){
            System.out.println("Windows is currently not supported. " +
                    "Please use the docker version. More information in readme.");
            System.exit(0);
        }

        Experiment experiment = experimentRepository.findById(experimentId).get();

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

        return startCornacInstance(name, modelClass, experiment, false);
    }

    public CornacInstanceDto startCornacInstance(String name, String modelClass, Experiment experiment, boolean isRestart) {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        String modelFolder = "uploads/" + modelClass + "-" + name + "/output/";
        int port;

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            // windows cmd file is not yet included.
            builder.command("cmd.exe", "cornac-run.cmd", Integer.toString(port), modelFolder, modelClass);
        } else {
            builder.command("sh", "cornac-run.sh", Integer.toString(port), modelFolder, modelClass);
        }

        try {
            Process process = builder.start();
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

                CornacInstance cornacInstance;
                if (isRestart) {
                    cornacInstance = updateCornacInstance(name, modelClass, port, experiment, process);
                } else {
                    cornacInstance = addCornacInstance(name, modelClass, port, experiment, process);
                }

                return new CornacInstanceDto(cornacInstance.getServiceName(), cornacInstance.getPort(), "Running" );
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

    public List<CornacInstance> getCornacInstancesForExperiment(Integer experimentId) {
        return cornacInstanceRepository.findCornacInstanceByExperimentId(experimentId);
    }

    public List<CornacInstanceDto> getCornacInstanceDtosForExperiment(Integer experimentId) {
        List<CornacInstance> cornacInstances = cornacInstanceRepository.findCornacInstanceByExperimentId(experimentId);
        return cornacInstances.stream()
                .map(cornacInstance -> convertToCornacInstanceDto(cornacInstance))
                .toList();
    }

    public List<CornacInstance> getCurrentActiveCornacInstancesForExperiment() {
        return activeCornacInstances;
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

    private CornacInstance updateCornacInstance(String name, String modelClass, Integer port, Experiment experiment, Process process) {
        WebClient webClient = WebClient.create(
                String.format("http://localhost:%d/", port)
        );

        CornacInstance cornacInstance = cornacInstanceRepository.findCornacInstanceByServiceNameAndModelClassAndExperimentId(name, modelClass, experiment.getId());
        cornacInstance.setProcess(process);
        cornacInstance.setPort(port);
        cornacInstance.setWebClient(webClient);

        cornacInstance = cornacInstanceRepository.save(cornacInstance);

        activeCornacInstances.add(cornacInstance);
        return cornacInstance;
    }

    private CornacInstance addCornacInstance(String name, String modelClass, Integer port, Experiment experiment, Process process) {
        WebClient webClient = WebClient.create(
                String.format("http://localhost:%d/", port)
        );

        CornacInstance cornacInstance = cornacInstanceRepository.save(new CornacInstance(name, modelClass, port, experiment, process, webClient));
        activeCornacInstances.add(cornacInstance);
        return cornacInstance;
    }

    public boolean isInstanceStillAlive (CornacInstance cornacInstance) {
        if (!activeCornacInstances.contains(cornacInstance)){
            return false;
        }

        CornacInstance localCornacInstance = activeCornacInstances.get(activeCornacInstances.indexOf(cornacInstance));
        if (localCornacInstance.getProcess() == null){
            return false;
        }

        return true;
    }

    public boolean removeCornacInstance(CornacInstance cornacInstance) {
        return activeCornacInstances.remove(cornacInstance);
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




}
