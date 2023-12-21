package ai.preferred.cornac.controller;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Feedback> getFeedbacks(@RequestParam(name = "userId", required = false) String userId,
                                       @RequestParam(defaultValue = "itemId", required = false) String itemId) {
        return feedbackService.getFeedbacks(userId, itemId);
    }

    public Feedback addFeedback(@RequestParam(name = "userId", required = false) String userId,
                                @RequestParam(defaultValue = "itemId", required = false) String itemId,
                                @RequestParam(name = "rating") String rating) {
        return null;
    }

}
