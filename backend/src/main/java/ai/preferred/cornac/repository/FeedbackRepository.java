




package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface FeedbackRepository extends CrudRepository<Feedback, String> {
    List<Feedback> findAll();
    List<Feedback> findAllByExperimentId(String experimentId);
    Stream<Feedback> findAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);
    Stream<Feedback> findAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqualAndModel(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore, String model);
    int countAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

    List<Feedback> findAllByExperimentIdAndUserIdAndAction(Integer experimentId, String userId, String action);
}
