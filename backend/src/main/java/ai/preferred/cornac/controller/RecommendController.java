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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RecommendLogDto getRecommendations(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "k", required = false) String k
    ){
        return recommendService.getRecommendations(userId, k);
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
