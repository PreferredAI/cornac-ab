package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/app")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public List<DemoUser> getUsers() {
        return applicationService.getUsers();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public DemoUser getUser(@PathVariable String userId){
        return applicationService.getUser(userId);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String userId){
        applicationService.deleteUser(userId);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public DemoUser updateUser(
            @PathVariable String userId,
            @RequestParam Map<String, String> attributes){
        return applicationService.updateUser(userId, attributes);
    }

    @RequestMapping(value = "/users/", method = RequestMethod.POST)
    public DemoUser createUser(
            @PathVariable String userId,
            @RequestParam Map<String, String> attributes){
        return applicationService.createUser(userId, attributes);
    }

    @RequestMapping(value = "/recommendations/", method = RequestMethod.GET)
    public AppRecommendation getRecommendations(
            @RequestParam String userId
    ) {
        return applicationService.getRecommendations(userId);
    }

    @RequestMapping(value = "/feedback/", method = RequestMethod.POST)
    public ResponseEntity postFeedback(
            @RequestParam String recommendId,
            @RequestParam String itemId,
            @RequestParam Integer rating,
            @RequestParam(defaultValue = "click") String action
    ) {
        Feedback feedback = applicationService.postFeedback(recommendId, itemId, rating, action);
        return ResponseEntity.created(URI.create("/app/feedback/" + feedback.getId())).build();
    }

}
