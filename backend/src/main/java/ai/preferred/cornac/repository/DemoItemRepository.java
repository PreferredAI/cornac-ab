package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.AppItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface DemoItemRepository extends CrudRepository<AppItem, String> {
    Stream<AppItem> findAllBy();
    List<AppItem> findAllByItemIdIn(List<String> itemIds);
}
