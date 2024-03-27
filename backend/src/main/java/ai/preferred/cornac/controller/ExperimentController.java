package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.entity.CornacEvaluationResponse;
import ai.preferred.cornac.entity.EvaluationRequest;
import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public Experiment getActiveExperiment() {
        return experimentService.getCurrentExperiment();
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
    public List<CornacEvaluationResponse> getEvaluationResult(@RequestBody EvaluationRequest request) {
        return experimentService.evaluateRecommendations(request);
    }
}
