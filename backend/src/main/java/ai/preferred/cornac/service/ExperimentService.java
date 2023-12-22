package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.entity.ExperimentStatus;
import ai.preferred.cornac.entity.ExperimentType;
import ai.preferred.cornac.repository.ExperimentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class ExperimentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExperimentService.class);

    @Autowired
    private ExperimentRepository experimentRepository;

    public List<Experiment> getExperiments(){
        return experimentRepository.findAll();
    }

    public Experiment getExperiment(String id){
        return experimentRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Experiment getCurrentExperiment(){
        return experimentRepository.findFirstByStartDateTimeLessThanEqualAndEndDateTimeIsGreaterThanEqual(new Date(), new Date());
    }

    public Experiment createNewExperiment(ExperimentType type, Date startDateTime, Date endDateTime, Long userSeed, Integer hoursSwitch){
        ExperimentStatus experimentStatus = ExperimentStatus.RUNNING;
        Experiment experiment = new Experiment(null, type, startDateTime, endDateTime, userSeed, hoursSwitch, experimentStatus);
        return experimentRepository.save(experiment);
    }
}
