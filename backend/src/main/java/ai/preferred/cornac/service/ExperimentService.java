package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.entity.ExperimentStatus;
import ai.preferred.cornac.entity.ExperimentType;
import ai.preferred.cornac.repository.ExperimentRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    public List<Experiment> getExperiments(){
        return experimentRepository.findAll();
    }

    public Experiment getExperiment(String id){
        return experimentRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Experiment getCurrentExperiment(){
        return experimentRepository.findFirstByEndDateTimeIsNull();
    }

    public Experiment createNewExperiment(Long userSeed){
        ExperimentStatus experimentStatus = ExperimentStatus.RUNNING;
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        Experiment experiment = new Experiment(null, startDateTime, null, userSeed, experimentStatus, new ArrayList<>());
        return experimentRepository.save(experiment);
    }
}
