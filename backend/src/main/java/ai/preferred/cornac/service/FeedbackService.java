package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.repository.OpensearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FeedbackService {
    private final OpensearchRepository feedbackRepository;

    public FeedbackService(OpensearchRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getFeedbacks(String userId, String itemId) {
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
