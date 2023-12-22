package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.Experiment;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ExperimentRepository extends CrudRepository<Experiment, Long> {
    List<Experiment> findAll();

    Experiment findFirstByStartDateTimeLessThanEqualAndEndDateTimeIsGreaterThanEqual(Date date1, Date date2);

}
