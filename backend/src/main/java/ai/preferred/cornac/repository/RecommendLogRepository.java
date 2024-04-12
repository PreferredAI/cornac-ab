package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.RecommendLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendLogRepository extends CrudRepository<RecommendLog, String> {
    List<RecommendLog> findAll();

}
