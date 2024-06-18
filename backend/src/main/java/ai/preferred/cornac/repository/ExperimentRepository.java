package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.Experiment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentRepository extends CrudRepository<Experiment, Integer> {
    List<Experiment> findAll();

    Experiment findFirstByEndDateTimeIsNull();

    List<Experiment> findAllByEndDateTimeIsNull();

}
