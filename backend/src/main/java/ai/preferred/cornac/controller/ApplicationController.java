package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.DemoItem;
import ai.preferred.cornac.entity.DemoUser;
import ai.preferred.cornac.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/demo")
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

    @RequestMapping(value = "/recommendations/{userId}", method = RequestMethod.GET)
    public List<DemoItem> getRecommendations(
            @PathVariable String userId
    ) {
        return applicationService.getRecommendations(userId);
    }
}
