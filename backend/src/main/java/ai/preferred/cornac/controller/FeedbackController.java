package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.EvaluationResult;
import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.entity.RecommendLog;
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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Feedback> getFeedbacks(@RequestParam(name = "userId", required = false) String userId,
                                       @RequestParam(defaultValue = "itemId", required = false) String itemId) {
        return null;
    }

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public List<Feedback> getFeedbacks(@RequestParam(name = "experimentId", required = false) String userId) {
        return null;
    }


    public RecommendLog addFeedback(@RequestParam(name = "userId", required = false) String userId,
                                    @RequestParam(defaultValue = "itemId", required = false) String itemId,
                                    @RequestParam(name = "rating") String rating) {
        return null;
    }

}
