package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.ExperimentDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.CornacInstanceRepository;
import ai.preferred.cornac.repository.ExperimentRepository;
import ai.preferred.cornac.util.TtestUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ExperimentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExperimentService.class);

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private CornacService cornacService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;

    @Autowired
    private TtestUtil ttestUtil;

    @Autowired
    private ModelMapper modelMapper;

    public List<Experiment> getExperiments(){
        return experimentRepository.findAll();
    }

    public Experiment getExperiment(String id){
        return experimentRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ExperimentDto getActiveExperiment(){
        return modelMapper.map(getCurrentExperiment(), ExperimentDto.class);
    }

    public Experiment getCurrentExperiment() {
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

    public EvaluationResult evaluateRecommendations(EvaluationRequest evaluationRequest) {
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

//        List<CornacEvaluationResponse> evaluationResponses = new ArrayList<>();

//        CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(null, cornacEvaluationRequest);
//        evaluationResponses.add(evaluationResponse);

        Map<String, CornacEvaluationResponse> modelNameToEvalResponse = new HashMap<>();
        List<String> metrics = new ArrayList<>();

        cornacInstances.forEach(cornacInstance -> {
            CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(cornacInstance, cornacEvaluationRequest);
            modelNameToEvalResponse.put(cornacInstance.getServiceName(), evaluationResponse);

            if (metrics.isEmpty()) {
                evaluationResponse.getResult().forEach((metric, userResult) -> {
                    metrics.add(metric);
                });
            }
        });

        // 3. Calculate T-test and p-value for each model pair
        List<TResult> tResultList = new ArrayList<>();

        for (String metric : metrics) {
            Map<String, TModelResult> modelToTModelResult = new HashMap<>();

            for (CornacInstance fromInstance : cornacInstances) {
                String fromModel = fromInstance.getServiceName();
                CornacEvaluationResponse fromEvaluationResponse = modelNameToEvalResponse.get(fromModel);
                Map<String, Double> fromUserResults = fromEvaluationResponse.getUserResult().get(metric);
                List<Double> fromValues = fromUserResults.values().stream().toList();

                for (CornacInstance toInstance : cornacInstances) {
                    String toModel = toInstance.getServiceName();
                    CornacEvaluationResponse toEvaluationResponse = modelNameToEvalResponse.get(toInstance.getServiceName());
                    Map<String, Double> toUserResults = toEvaluationResponse.getUserResult().get(metric);
                    List<Double> toValues = toUserResults.values().stream().toList();

                    double tVal = calculateTTest(fromValues.stream().mapToDouble(Double::doubleValue).toArray(),
                            toValues.stream().mapToDouble(Double::doubleValue).toArray());
                    double pVal = calculatePValue(fromValues.stream().mapToDouble(Double::doubleValue).toArray(),
                            toValues.stream().mapToDouble(Double::doubleValue).toArray());

                    PValue pValue = new PValue(tVal, pVal);

                    TModelResult tModelResult;
                    if (modelToTModelResult.containsKey(fromModel)) {
                        tModelResult = modelToTModelResult.get(fromModel);
                    } else {
                        tModelResult = new TModelResult();
                        tModelResult.setModel(fromModel);
                        tModelResult.setToModelPValues(new HashMap<>());
                    }
                    tModelResult.getToModelPValues().put(toModel, pValue);
                    modelToTModelResult.put(fromModel, tModelResult);
                }
            }
            tResultList.add(new TResult(metric, new ArrayList<>(modelToTModelResult.values())));
        }

        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.setMetrics(metrics);

        List<MetricResult> metricResults = new ArrayList<>();
        modelNameToEvalResponse.forEach((model, evalResponse) -> {
            metricResults.add(new MetricResult(model, evalResponse.getResult()));
        });

        evaluationResult.setModelMetricResults(metricResults);
        evaluationResult.setTResults(tResultList);

        return evaluationResult;
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

    public Double calculateTTest(double[] a, double[] b) {
        return ttestUtil.pairedTTest(a, b);
    }

    public Double calculatePValue(double[] a, double[] b) {
        return ttestUtil.pairedPvalue(a, b);
    }
}
