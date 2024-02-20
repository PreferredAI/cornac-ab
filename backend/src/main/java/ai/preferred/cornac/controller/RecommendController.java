package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.service.RecommendService;
import ai.preferred.cornac.service.CornacService;
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

    @Autowired
    private CornacService cornacService;


    @RequestMapping(value = "/instance", method = RequestMethod.POST)
    public CornacInstanceDto createCornacInstance(
            @RequestParam(name = "name", defaultValue = "model") String name,
            @RequestParam(name = "modelClass") String modelClass,
            @RequestParam(name = "experimentId") Long experimentId,
            @RequestParam("file") MultipartFile file) {
        System.out.println(name);
        System.out.println(modelClass);
        System.out.println(file.getName());
        return cornacService.createCornacInstance(name, modelClass, experimentId, file);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RecommendLogDto getRecommendations(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "k", required = false) String k
    ){
        return recommendService.getRecommendations(userId, k);
    }

}
