package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.DemoUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface DemoUserRepository extends CrudRepository<DemoUser, String> {
    Stream<DemoUser> findAllBy();
}
