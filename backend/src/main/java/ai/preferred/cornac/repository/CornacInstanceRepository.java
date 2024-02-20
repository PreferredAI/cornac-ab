package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.CornacInstance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CornacInstanceRepository extends CrudRepository<CornacInstance, Long> {
    List<CornacInstance> findCornacInstanceByExperimentId(Integer experimentId);
    CornacInstance findCornacInstanceByServiceNameAndModelClassAndExperimentId(String serviceName, String modelClass, Integer experimentId);

}
