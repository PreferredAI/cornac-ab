package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.entity.CornacInstance;
import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.entity.ExperimentStatus;
import ai.preferred.cornac.repository.CornacInstanceRepository;
import ai.preferred.cornac.repository.ExperimentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExperimentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExperimentService.class);

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private CornacService cornacService;
    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;

    public List<Experiment> getExperiments(){
        return experimentRepository.findAll();
    }

    public Experiment getExperiment(String id){
        return experimentRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Experiment getCurrentExperiment(){
        return experimentRepository.findFirstByEndDateTimeIsNull();
    }

    public List<CornacInstanceDto> getCornacInstances() {
        Experiment experiment = getCurrentExperiment();
        if (experiment == null) {
            return new ArrayList<>();
        }
        return cornacService.getCornacInstanceDtosForExperiment(experiment.getId());
    }

    @Transactional
    public Experiment createNewExperiment(Long userSeed, List<String> modelName, List<String> modelClass, List<MultipartFile> file){
        // 1. First create and save experiment instance
        Experiment experiment = saveExperiment(userSeed);

        // 2. Create cornac instances for each model uploaded
        for (int i = 0; i < modelName.size(); i++) {
            cornacService.createCornacInstance(modelName.get(i), modelClass.get(i), experiment.getId(), file.get(i));
        }
        // 3. Allocate users into instances and start the experiment
        cornacService.allocateUsersToInstances(experiment);

        return experiment;
    }

    @Transactional
    public Experiment saveExperiment(Long userSeed) {
        ExperimentStatus experimentStatus = ExperimentStatus.RUNNING;
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        Experiment experiment = new Experiment(startDateTime, null, userSeed, experimentStatus);
        return experimentRepository.save(experiment);
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void checkExistingExperiment(){
        Experiment experiment = experimentRepository.findFirstByEndDateTimeIsNull();
        if (experiment == null) {
            LOGGER.info("There are no running experiments.");
            return;
        }

        List<CornacInstance> cornacInstances =
                cornacInstanceRepository.findCornacInstanceByExperimentId(experiment.getId());

        if (experiment.getCornacInstances() == null) {
            LOGGER.info("There are no running cornac instances.");
            return;
        } else {
            LOGGER.info("There are {} running cornac instances.", cornacInstances.size());
        }

        for (CornacInstance cornacInstance : cornacInstances) {

            if (!cornacService.isInstanceStillAlive(cornacInstance)) {
                // kill the local instance, and restart it.
                cornacService.removeInMemoryCornacInstance(cornacInstance);

                LOGGER.info("Restarting Cornac instance {}", cornacInstance.getServiceName());

                cornacService.startCornacInstance(cornacInstance.getServiceName(), cornacInstance.getModelClass(), experiment, true);

                return;
            }
        }
    }
}
