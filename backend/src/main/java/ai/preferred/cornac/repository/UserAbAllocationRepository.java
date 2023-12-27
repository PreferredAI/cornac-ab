package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.UserAbAllocation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAbAllocationRepository extends CrudRepository<UserAbAllocation, Long> {
    List<UserAbAllocation> findAll();

    UserAbAllocation findByExperimentIdAndUserId(Integer experimentId, String userId);
    Integer countByExperimentId(Integer experimentId);

}
