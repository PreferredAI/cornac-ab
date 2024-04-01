package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.CornacInstanceRepository;
import ai.preferred.cornac.repository.RecommendLogRepository;
import ai.preferred.cornac.repository.UserAbAllocationRepository;
import jakarta.annotation.PreDestroy;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecommendService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendService.class);

    @Autowired
    private RecommendLogRepository recommendLogRepository;

    @Autowired
    private UserAbAllocationRepository userAbAllocationRepository;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CornacService cornacService;

    @Autowired
    private ModelMapper modelMapper;

    private Integer getAbInstanceGroup(Experiment experiment, String userId){
        if (experiment.isUserIndexFound()) {
            return getPreassignedAbInstance(experiment, userId);
        } else {
            return getAbInstanceOnTheFly(experiment, userId);
        }
    }

    private Integer getPreassignedAbInstance(Experiment experiment, String userId) {
        UserAbAllocation userAbAllocation = userAbAllocationRepository.findByExperimentIdAndUserId(experiment.getId(), userId);
        if (userAbAllocation == null) {
            return -1;
        } else {
            return userAbAllocation.getAbGroup();
        }
    }

    private Integer getAbInstanceOnTheFly(Experiment experiment, String userId) {
        Integer experimentId = experiment.getId();

        int numInstances = cornacService.getCornacInstancesForExperiment(experimentId).size();

        UserAbAllocation userAbAllocation = userAbAllocationRepository.findByExperimentIdAndUserId(experimentId, userId);

        if (userAbAllocation == null) {
            // allocate an ab-testing group
            // - abGroup is a random number between 0 and numInstances
            // - this is a deterministic random operation.
            // - This will give the same instance on the same seed for the same user.
            Random rand = new Random(experiment.getUserSeed());
            int randInt = rand.nextInt(userId.hashCode());
            int abGroup = randInt % numInstances;
            userAbAllocationRepository.save(new UserAbAllocation(null, userId, abGroup, experiment));
            return abGroup;
        } // if its not null, it has been previously allocated and cached in db

        return userAbAllocation.getAbGroup();
    }

    public RecommendLogDto getRecommendations(String userId, String k){
        Experiment experiment = experimentService.getCurrentExperiment();

        if (experiment == null){
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                    new RuntimeException("No experiment is currently running.")
            );
        }

        Integer chosenInstance = getAbInstanceGroup(experiment, userId);
//        Integer chosenInstance = 0;
//        System.out.println(experiment.getId());

//        CornacInstance cornacInstance = cornacService.getCornacInstancesForExperiment(experiment.getId()).get(chosenInstance);
        List<CornacInstance> cornacInstances = cornacService.getInMemoryCornacInstances();

        CornacInstance cornacInstance;
        try {
            cornacInstance = cornacInstances.get(chosenInstance);
        } catch (ArrayIndexOutOfBoundsException e){
            LOGGER.warn("Cornac instance not found, applying fallback solution.");
            cornacInstance = null;
        }

        RecommendLog log = new RecommendLog();
        Recommendation recommendation;
        try{
            recommendation = cornacService.getApiRecommendation(cornacInstance, userId, k);
        } catch (Exception e){
            LOGGER.error("Error occurred while getting recommendations", e);
            LOGGER.warn("Applying fallback solution, getting top items.");
            int top_k = Integer.parseInt(k);
            List<String> resultIds = feedbackService.getTopItems(experiment.getId(), top_k);

            log.setFallback(true);
            log.setFallbackReason("exception");

            if (resultIds.size() < top_k){
                LOGGER.warn("Not enough top items found, getting random items.");
                resultIds = feedbackService.getRandomItems(top_k);
                log.setFallbackReason("not_enough_top_items");
            }

            recommendation = new Recommendation(resultIds, new RecommendationQuery(userId, k, false));
        }

        log.setExperimentId(experiment.getId());
        log.setUserId(userId);
        log.setTimestamp(LocalDateTime.now());
        log.setRecommendations(recommendation.getRecommendations());

        RecommendLog recommendLog = recommendLogRepository.save(log);
        return convertToRecommendLogDto(recommendLog);
    }

    private RecommendLogDto convertToRecommendLogDto(RecommendLog recommendLog) {
        return modelMapper.map(recommendLog, RecommendLogDto.class);
    }





    @PreDestroy
    public void destroy(){
        System.out.println("destroy called, closing all cornac instances.");
        cornacService.getInMemoryCornacInstances().forEach(cornacInstance -> {
            cornacInstance.getProcess().destroy();
//            cornacInstance.setStopped(true);
            cornacService.updateCornacInstanceStatus(cornacInstance.getId(), "suspended");
        });

    }


}
