




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

//    List<Feedback> findAllByExperimentIdAndTimestampAfterAndTimestampBefore(String experimentId, LocalDate timestampAfter, LocalDate timestampBefore);
    Window<Feedback> findAllByExperimentIdAndTimestampAfterAndTimestampBefore(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore, ScrollPosition position);
    Page<Feedback> findAllByExperimentIdAndTimestampAfterAndTimestampBefore(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore, Pageable pageable);
//    Stream<Feedback> findAllByExperimentIdAndTimestampAfterAndTimestampBefore(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);
    Stream<Feedback> findAllByExperimentIdAndTimestampGreaterThanEqualAndTimestampLessThanEqual(String experimentId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

}
