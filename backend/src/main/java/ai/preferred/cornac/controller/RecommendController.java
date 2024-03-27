package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.service.RecommendService;
import ai.preferred.cornac.service.CornacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private CornacService cornacService;


//    @Transactional
//    @RequestMapping(value = "/instance", method = RequestMethod.POST)
//    public CornacInstanceDto createCornacInstance(
//            @RequestParam(name = "name", defaultValue = "model") String name,
//            @RequestParam(name = "modelClass") String modelClass,
//            @RequestParam(name = "experimentId") Integer experimentId,
//            @RequestParam("file") MultipartFile file) {
//        System.out.println(name);
//        System.out.println(modelClass);
//        System.out.println(file.getName());
//        return cornacService.createCornacInstance(name, modelClass, experimentId, file);
//    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RecommendLogDto getRecommendations(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "k", required = false) String k
    ){
        return recommendService.getRecommendations(userId, k);
    }

    @RequestMapping(value = "/evaluate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CornacEvaluationResponse> getEvaluationResult(@RequestBody EvaluationRequest request) {
        return recommendService.evaluateRecommendations(request);
    }
//    public List<EvaluationResponse> getEvaluationResult(@RequestParam(name = "metrics") List<MetricRequest> metrics,
//                                                        @RequestParam(name = "experimentId") String experimentId,
//                                                        @RequestParam(name = "dateFrom") DateTime dateFrom,
//                                                        @RequestParam(name = "dateTo") DateTime dateTo) {
//
//        return recommendService.evaluateRecommendations(metrics, experimentId, dateFrom, dateTo);
//    }

//    @RequestMapping(value = "/instance", method = RequestMethod.GET)
//    public List<CornacInstanceDto> getCornacInstances() {
//        return cornacService.getInMemoryCornacInstances();
//    }

}
