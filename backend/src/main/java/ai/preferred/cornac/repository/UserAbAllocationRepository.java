package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.UserAbAllocation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAbAllocationRepository extends ListCrudRepository<UserAbAllocation, Long> {
    List<UserAbAllocation> findAll();

    UserAbAllocation findByExperimentIdAndUserId(Integer experimentId, String userId);
    Integer countByExperimentId(Integer experimentId);

}
