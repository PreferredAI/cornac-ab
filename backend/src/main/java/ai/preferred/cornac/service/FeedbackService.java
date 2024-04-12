package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private RecommendLogRepository recommendLogRepository;
    @Autowired
    private UserAbAllocationRepository userAbAllocationRepository;
    @Autowired
    private CornacInstanceRepository cornacInstanceRepository;
    @Autowired
    private FeedbackRepositoryImpl feedbackRepositoryImpl;

    public Feedback addFeedback(String recommendId, String itemId, Integer rating, String action) {
        RecommendLog recommendLog = recommendLogRepository.findById(recommendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommend ID not found"));

        UserAbAllocation userAbAllocation = userAbAllocationRepository.findByExperimentIdAndUserId(
                recommendLog.getExperimentId(), recommendLog.getUserId()
        );
        List<CornacInstance> cornacInstances = cornacInstanceRepository.findCornacInstanceByExperimentId(recommendLog.getExperimentId());

        String cornacInstanceName;
        if (userAbAllocation == null) {
            cornacInstanceName = "unallocated";
        } else {
            cornacInstanceName = cornacInstances.get(userAbAllocation.getAbGroup()).getServiceName();
        }

        Feedback feedback = new Feedback();
        feedback.setItemId(itemId);
        feedback.setRating(rating);
        feedback.setUserId(recommendLog.getUserId());
        feedback.setTimestamp(LocalDateTime.now());
        feedback.setExperimentId(recommendLog.getExperimentId());
        feedback.setModel(cornacInstanceName);
        feedback.setAction(action);

        return feedbackRepository.save(feedback);
    }
    public List<Feedback> getFeedbacks(String experimentId) {
        return feedbackRepository.findAllByExperimentId(experimentId);
    }

    public List<Feedback> getFeedbacks(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore, String model) {
        List<Feedback> feedbacks = new ArrayList<>();
//        final int PAGE_SIZE = 10000;
        try (Stream<Feedback> feedbackStream = feedbackRepository.findAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqualAndModel(experimentId, timestampAfter, timestampBefore, model)){
            feedbacks.addAll(feedbackStream.toList());
        }

        return feedbacks;
    }

    public List<String> getTopItems(Integer experimentId, int limit) {
        return feedbackRepositoryImpl.topItems(experimentId, limit);
    }

    public List<String> getRandomItems(int limit){
        return feedbackRepositoryImpl.randomItems(limit);
    }

    public List<FeedbackModelSummary> getFeedbackSummary(List<String> models, String experimentId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return feedbackRepositoryImpl.getFeedbackModelSummary(Integer.parseInt(experimentId), models, dateFrom.toString(), dateTo.toString());
    }

    public List<Feedback> getPastRatings(Integer experimentId, String userId) {
        return feedbackRepository.findAllByExperimentIdAndUserIdAndAction(experimentId, userId, "rating");
    }

}
