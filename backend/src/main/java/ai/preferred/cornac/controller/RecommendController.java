package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;


    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public List<CornacInstanceDto> getCornacInstances() {
        return recommendService.getCornacInstances();
    }

    @RequestMapping(value = "/instance", method = RequestMethod.POST)
    public CornacInstanceDto createCornacInstance(
            @RequestParam(name = "name", defaultValue = "model") String name,
            @RequestParam(name = "modelClass") String modelClass,
            @RequestParam("file") MultipartFile file) {
        return recommendService.createCornacInstance(name, modelClass, file);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RecommendLogDto getRecommendations(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "k", required = false) String k
    ){
        return recommendService.getRecommendations(userId, k);
    }

}
