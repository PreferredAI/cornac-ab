package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.entity.RecommendLog;
import ai.preferred.cornac.repository.FeedbackRepository;
import ai.preferred.cornac.repository.RecommendLogRepository;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    private final RecommendLogRepository recommendLogRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, RecommendLogRepository recommendLogRepository) {
        this.feedbackRepository = feedbackRepository;
        this.recommendLogRepository = recommendLogRepository;
    }

    public Feedback addFeedback(String recommendId, String itemId, Integer rating) {
        RecommendLog recommendLog = recommendLogRepository.findById(recommendId);

        if (recommendLog == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not found");
        }

        Feedback feedback = new Feedback();
        feedback.setItemId(itemId);
        feedback.setRating(rating);
        feedback.setUserId(recommendLog.getUserId());
        feedback.setTimestamp(DateTime.now());

        return feedbackRepository.save(feedback);
    }
    public List<Feedback> getFeedbacks(Long experimentId) {
        return feedbackRepository.findAllByExperimentId(experimentId);
    }


}
