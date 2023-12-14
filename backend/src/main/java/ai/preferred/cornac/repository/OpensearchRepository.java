package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.Feedback;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpensearchRepository extends CrudRepository<Feedback, Long> {
    List<Feedback> findAll();
    List<Feedback> findAllByUserId(String userId);

}
