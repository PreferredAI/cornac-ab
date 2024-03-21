package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.entity.RecommendLog;
import ai.preferred.cornac.repository.FeedbackRepository;
import ai.preferred.cornac.repository.RecommendLogRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        feedback.setTimestamp(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }
    public List<Feedback> getFeedbacks(String experimentId) {
        return feedbackRepository.findAllByExperimentId(experimentId);
    }

    public List<Feedback> getFeedbacks(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore) {
        List<Feedback> feedbacks = new ArrayList<>();
//        final int PAGE_SIZE = 10000;
        try (Stream<Feedback> feedbackStream = feedbackRepository.findAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(experimentId, timestampAfter, timestampBefore)){
            feedbacks.addAll(feedbackStream.toList());
        }

        return feedbacks;
    }

}
