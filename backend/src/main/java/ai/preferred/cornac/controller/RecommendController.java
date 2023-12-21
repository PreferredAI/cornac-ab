package ai.preferred.cornac.controller;

import ai.preferred.cornac.dto.CornacInstanceDto;
import ai.preferred.cornac.dto.RecommendationDto;
import ai.preferred.cornac.service.RecommendService;
import org.springframework.data.repository.core.support.FragmentNotImplementedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

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
    public RecommendationDto getRecommendations(){
        return null;
    }

}
