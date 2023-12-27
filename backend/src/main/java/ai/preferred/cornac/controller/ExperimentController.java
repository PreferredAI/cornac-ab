package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.Experiment;
import ai.preferred.cornac.entity.ExperimentType;
import ai.preferred.cornac.entity.RecommendLog;
import ai.preferred.cornac.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
                                          @RequestParam(name = "userSeed", defaultValue = "123") Long userSeed) {
        return experimentService.createNewExperiment(userSeed);
    }

}
