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
    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;

    private Integer getAbInstanceGroup(Experiment experiment, String userId){
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
        }

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
        CornacInstance cornacInstance = cornacInstances.get(chosenInstance);

        Recommendation recommendation;
        try{
            recommendation = cornacService.getApiRecommendation(cornacInstance, userId, k);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            throw e;
        }

        RecommendLog log = new RecommendLog();
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

    public List<CornacEvaluationResponse> evaluateRecommendations(EvaluationRequest evaluationRequest) {
        // 1. Get feedbacks
        List<Feedback> feedbacks = feedbackService.getFeedbacks(
                evaluationRequest.getExperimentId(), evaluationRequest.getDateFrom(), evaluationRequest.getDateTo()
        );
        System.out.println(evaluationRequest.getExperimentId());
        System.out.println(evaluationRequest.getDateFrom());
        System.out.println(evaluationRequest.getDateTo());
        System.out.println("Feedbacks: " + feedbacks.size());

        if (feedbacks.isEmpty()) {
            throw new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("No feedbacks found"));
        }

        System.out.println("SAMPLE FEEDBACK ===");
        System.out.println(feedbacks.get(0));

        CornacEvaluationRequest cornacEvaluationRequest = convertToCornacEvaluationRequest(evaluationRequest, feedbacks);

        // 2. Send feedbacks to the Cornac evaluation service for each model
        List<CornacInstance> cornacInstances = cornacService.getInMemoryCornacInstances();

        List<CornacEvaluationResponse> evaluationResponses = new ArrayList<>();

//        CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(null, cornacEvaluationRequest);
//        evaluationResponses.add(evaluationResponse);

        cornacInstances.forEach(cornacInstance -> {
            CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(cornacInstance, cornacEvaluationRequest);
            evaluationResponses.add(evaluationResponse);
        });

        return evaluationResponses;
    }

    private CornacEvaluationRequest convertToCornacEvaluationRequest(EvaluationRequest evaluationRequest, List<Feedback> feedbacks) {
        List<List<Object>> data = new ArrayList<>();
        feedbacks.forEach(feedback -> {
            List<Object> feedbackData = new ArrayList<>();
            feedbackData.add(feedback.getUserId());
            feedbackData.add(feedback.getItemId());
            feedbackData.add(feedback.getRating());
            data.add(feedbackData);
        });

        List<String> metrics = new ArrayList<>();
        evaluationRequest.getMetrics().forEach(metricRequest -> {
            metrics.add(metricRequest.getMetric());
        });

        return new CornacEvaluationRequest(metrics, data);
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy called, closing all cornac instances.");
        cornacService.getInMemoryCornacInstances().forEach(cornacInstance -> {
            cornacInstance.getProcess().destroy();
//            cornacInstance.setStopped(true);
//            cornacInstanceRepository.save(cornacInstance);
        });

    }


}
