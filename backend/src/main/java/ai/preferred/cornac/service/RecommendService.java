package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.entity.CornacInstance;
import jakarta.annotation.PreDestroy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendService.class);
    private final List<CornacInstance> activeCornacInstances = new ArrayList<>();

    @Autowired
    private ModelMapper modelMapper;

    public CornacInstanceDto createCornacInstance(String name, int port) {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "cornac-run.cmd");
        } else {
            builder.command("sh", "cornac-run.sh");
        }

        try {
            Process process = builder.start();
            addCornacInstance(name, port, process);

            System.out.println("waiting");
            System.out.println(process.waitFor(10, TimeUnit.SECONDS));
            return new CornacInstanceDto(name, port, "created");
//            List<String> outputs = readOutput(process.getInputStream());
//            List<String> errors = readOutput(process.getErrorStream());
//            outputs.forEach(output -> LOGGER.info("cornac instance: " + output));
//            errors.forEach(error -> LOGGER.error("cornac instance: " + error));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        try {
//            ProcessBuilder builder = new ProcessBuilder();
//            if (isWindows) {
//                builder.command("cmd.exe", "/c", "dir");
//            } else {
//                builder.command("sh", "-c", "ls");
//            }
//            builder.directory(new File(System.getProperty("user.home")));
//            Process process = builder.start();
//            try {
//                process.waitFor(100, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
//                    process.getInputStream())
//            );
//
//            System.out.println("t");
//
//            String s = null;
//            while ((s = stdInput.readLine()) != null) {
//                System.out.println(s);
//            }
//
////            List<String> results = readOutput(process.getInputStream());
//            return null;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
        activeCornacInstances.add(new CornacInstance(name, port, process));
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy called, closing all cornac instances.");
        activeCornacInstances.forEach(cornacInstance -> cornacInstance.getProcess().destroy());
    }


}
