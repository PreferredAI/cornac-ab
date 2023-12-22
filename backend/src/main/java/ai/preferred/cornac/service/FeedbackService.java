package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.RecommendLog;
import ai.preferred.cornac.repository.RecommendLogRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FeedbackService {
    private final RecommendLogRepository feedbackRepository;

    public FeedbackService(RecommendLogRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<RecommendLog> getFeedbacks(String userId, String itemId) {
        return feedbackRepository.findAll(); //TODO: filter
    }

    public void createCornacInstance() {
        try {
            Process process = new ProcessBuilder("java", "-version").start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
