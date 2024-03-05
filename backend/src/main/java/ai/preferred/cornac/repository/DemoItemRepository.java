package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.DemoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface DemoItemRepository extends CrudRepository<DemoItem, String> {
    Stream<DemoItem> findAllBy();
    List<DemoItem> findAllByItemIdIn(List<String> itemIds);
}
