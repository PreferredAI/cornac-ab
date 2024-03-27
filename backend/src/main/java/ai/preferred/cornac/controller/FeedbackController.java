package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public List<Feedback> getFeedbacks(@PathVariable(name = "experimentId", required = false) String experimentId) {
        return feedbackService.getFeedbacks(experimentId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Feedback addFeedback(@RequestParam(name = "recommendId") String recommendId,
                                    @RequestParam(defaultValue = "itemId") String itemId,
                                    @RequestParam(name = "rating") String rating) {
        return feedbackService.addFeedback(recommendId, itemId, Integer.parseInt(rating));
    }

    @RequestMapping(value = "/topItems", method = RequestMethod.GET)
    public List<String> topItems(@RequestParam(name = "experimentId") int experimentId,
                                 @RequestParam(name = "limit") int limit) {
        return feedbackService.getTopItems(experimentId, limit);
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public List<String> randomItems(@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        return feedbackService.getRandomItems(limit);
    }

}
