package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.ExperimentDto;
import ai.preferred.cornac.dto.UserAbAllocationDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/experiment")
public class ExperimentController {
    @Autowired
    private ExperimentService experimentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Experiment> getExperiments() {
        return experimentService.getExperiments();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ExperimentDto getActiveExperiment() {
        return experimentService.getActiveExperiment();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Experiment getExperiment(@PathVariable String id){
        return experimentService.getExperiment(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Experiment createNewExperiment(
//                                            @RequestParam(name = "type") ExperimentType type,
//                                          @RequestParam(name = "startDateTime") Date startDateTime,
//                                          @RequestParam(name = "endDateTime") Date endDateTime,
                                          @RequestParam(name = "userSeed", defaultValue = "123") Long userSeed,
                                          @RequestParam(name = "name", defaultValue = "model") List<String> modelName,
                                          @RequestParam(name = "modelClass") List<String> modelClass,
                                          @RequestParam("file") List<MultipartFile> file) {
        return experimentService.createNewExperiment(userSeed, modelName, modelClass, file);
    }

    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public List<CornacInstanceDto> getCornacInstances() {
        return experimentService.getCornacInstances();
    }

    @RequestMapping(value = "/evaluate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EvaluationResult postEvaluationResults(@RequestBody EvaluationRequest request) {
        return experimentService.evaluateRecommendations(request);
    }

    @RequestMapping(value = "/evaluate/raw", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, CornacEvaluationResponse> postEvaluationResultsRaw(@RequestBody EvaluationRequest request) {
        return experimentService.evaluateRecommendationsRaw(request);
    }

    @RequestMapping(value = "/ttest", method = RequestMethod.GET)
    public Double doTTest(@RequestParam(name = "a") double[] a, @RequestParam(name = "b") double[] b) {
        return experimentService.calculateTTest(a, b);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserAbAllocationDto> getUserAbAllocations() {
        return experimentService.getUserAbAllocations();
    }

    @RequestMapping(value = "/feedback/summary", method = RequestMethod.GET)
    public List<FeedbackModelSummary> getFeedbackSummary(@RequestParam List<String> models,
                                                         @RequestParam String experimentId,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return experimentService.getFeedbackSummary(models, experimentId, dateFrom, dateTo);
    }
}
