package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.ExperimentDto;
import ai.preferred.cornac.dto.UserAbAllocationDto;
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
import java.time.LocalDateTime;
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
        Experiment experiment = getCurrentExperiment();
        if (experiment == null) {
            throw new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("No running experiment found"));
        }
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

    public Map<String, CornacEvaluationResponse> evaluateRecommendationsRaw(EvaluationRequest evaluationRequest) {
        // Do sanity checks
        if (evaluationRequest.getModels().isEmpty()) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No models provided"));
        }

        if (evaluationRequest.getMetrics().isEmpty()) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No metrics provided"));
        }

        if (evaluationRequest.getExperimentId() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No experiment ID provided"));
        }

        if (evaluationRequest.getDateFrom() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No dateFrom provided"));
        }

        if (evaluationRequest.getDateTo() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No dateTo provided"));
        }

        List<CornacInstance> cornacInstances = cornacService.getInMemoryCornacInstances();

        List<String> models = evaluationRequest.getModels();
        models.forEach(model -> {
            if (cornacInstances.stream().noneMatch(cornacInstance -> cornacInstance.getServiceName().equals(model))) {
                throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("Model " + model + " not found"));
            }
        });

        Map<String, CornacEvaluationResponse> modelNameToEvalResponse = new HashMap<>();
        List<String> metrics = new ArrayList<>();

        models.forEach(model -> {
            // 1. Get feedbacks for each model
            List<Feedback> feedbacks = feedbackService.getFeedbacks(
                    evaluationRequest.getExperimentId(), evaluationRequest.getDateFrom(), evaluationRequest.getDateTo(), model
            );
            System.out.println("Feedbacks for model " + model + ": " + feedbacks.size());

            if (feedbacks.isEmpty()) {
                throw new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("No feedbacks found for model: " + model));
            }

            // 2. get cornac instance for model
            CornacInstance cornacInstance = cornacInstances.stream().filter(
                    cornacInstance1 -> cornacInstance1.getServiceName().equals(model)
            ).findFirst().orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("Model " + model + " not found")));

            // 3, prepare evaluation request
            CornacEvaluationRequest cornacEvaluationRequest = convertToCornacEvaluationRequest(evaluationRequest, feedbacks);

            // 4. do evaluation on cornac instance
            CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(cornacInstance, cornacEvaluationRequest);
            modelNameToEvalResponse.put(cornacInstance.getServiceName(), evaluationResponse);

            // metric names on model results differ from request, create a list of metric names
            if (metrics.isEmpty()) {
                evaluationResponse.getResult().forEach((metric, userResult) -> {
                    metrics.add(metric);
                });
            }

        });

        return modelNameToEvalResponse;
    }
    public EvaluationResult evaluateRecommendations(EvaluationRequest evaluationRequest) {
        // Do sanity checks
        if (evaluationRequest.getModels().isEmpty()) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No models provided"));
        }

        if (evaluationRequest.getMetrics().isEmpty()) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No metrics provided"));
        }

        if (evaluationRequest.getExperimentId() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No experiment ID provided"));
        }

        if (evaluationRequest.getDateFrom() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No dateFrom provided"));
        }

        if (evaluationRequest.getDateTo() == null) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("No dateTo provided"));
        }

        List<CornacInstance> cornacInstances = cornacService.getInMemoryCornacInstances();

        List<String> models = evaluationRequest.getModels();
        models.forEach(model -> {
            if (cornacInstances.stream().noneMatch(cornacInstance -> cornacInstance.getServiceName().equals(model))) {
                throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new RuntimeException("Model " + model + " not found"));
            }
        });

        Map<String, CornacEvaluationResponse> modelNameToEvalResponse = new HashMap<>();
        List<String> metrics = new ArrayList<>();

        models.forEach(model -> {
            // 1. Get feedbacks for each model
            List<Feedback> feedbacks = feedbackService.getFeedbacks(
                    evaluationRequest.getExperimentId(), evaluationRequest.getDateFrom(), evaluationRequest.getDateTo(), model
            );
            System.out.println("Feedbacks for model " + model + ": " + feedbacks.size());

            if (feedbacks.isEmpty()) {
                throw new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("No feedbacks found for model: " + model));
            }

            // 2. get cornac instance for model
            CornacInstance cornacInstance = cornacInstances.stream().filter(
                    cornacInstance1 -> cornacInstance1.getServiceName().equals(model)
            ).findFirst().orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("Model " + model + " not found")));

            // 3, prepare evaluation request
            CornacEvaluationRequest cornacEvaluationRequest = convertToCornacEvaluationRequest(evaluationRequest, feedbacks);

            // 4. do evaluation on cornac instance
            CornacEvaluationResponse evaluationResponse = cornacService.postCornacInstanceEvaluation(cornacInstance, cornacEvaluationRequest);
            modelNameToEvalResponse.put(cornacInstance.getServiceName(), evaluationResponse);

            // metric names on model results differ from request, create a list of metric names
            if (metrics.isEmpty()) {
                evaluationResponse.getResult().forEach((metric, userResult) -> {
                    metrics.add(metric);
                });
            }

        });

        // 5. Calculate T-test and p-value for each model pair
        List<TResult> tResultList = new ArrayList<>();

        for (String metric : metrics) {
            Map<String, TModelResult> modelToTModelResult = new HashMap<>();

            for (String fromModel : modelNameToEvalResponse.keySet()) {
                CornacEvaluationResponse fromEvaluationResponse = modelNameToEvalResponse.get(fromModel);
                Map<String, Double> fromUserResults = fromEvaluationResponse.getUserResult().get(metric);
                List<Double> fromValues = fromUserResults.values().stream().toList();

                for (String toModel : modelNameToEvalResponse.keySet()) {

                    PValue pValue;

                    if (fromModel.equals(toModel)) {
                        pValue = new PValue(0.0, 1.0);
                    } else {
                        CornacEvaluationResponse toEvaluationResponse = modelNameToEvalResponse.get(toModel);
                        Map<String, Double> toUserResults = toEvaluationResponse.getUserResult().get(metric);
                        List<Double> toValues = toUserResults.values().stream().toList();

                        double tVal = calculateTTest(fromValues.stream().mapToDouble(Double::doubleValue).toArray(),
                                toValues.stream().mapToDouble(Double::doubleValue).toArray());
                        double pVal = calculatePValue(fromValues.stream().mapToDouble(Double::doubleValue).toArray(),
                                toValues.stream().mapToDouble(Double::doubleValue).toArray());

                        pValue = new PValue(tVal, pVal);
                    }

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

        // 6. Lastly, prepare entity for returning the results

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

        List<String> metrics = evaluationRequest.getMetrics();

        return new CornacEvaluationRequest(metrics, data);
    }

    public Double calculateTTest(double[] a, double[] b) {
        return ttestUtil.ttest(a, b);
    }

    public Double calculatePValue(double[] a, double[] b) {
        return ttestUtil.pValue(a, b);
    }

    public List<UserAbAllocationDto> getUserAbAllocations() {
        Experiment experiment = getCurrentExperiment();
        if (experiment == null) {
            return new ArrayList<>();
        }
//        return cornacService.getUserAbAllocations(experiment.getId());
        return new ArrayList<>(); // to implement
    }

    public List<FeedbackModelSummary> getFeedbackSummary(List<String> models, String experimentId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return feedbackService.getFeedbackSummary(models, experimentId, dateFrom, dateTo);
    }

    public List<Feedback> getPastRatings(String userId) {
        Experiment experiment = getCurrentExperiment();
        if (experiment == null) {
            throw new ErrorResponseException(HttpStatus.NOT_FOUND, new RuntimeException("No running experiment found"));
        }
        return feedbackService.getPastRatings(experiment.getId(), userId);
    }
}
