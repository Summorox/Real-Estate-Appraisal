package pt.ipp.isep.repository;

import org.springframework.data.repository.CrudRepository;
import pt.ipp.isep.model.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Integer> {
    List<Item> findAllByGroupId(Integer groupId);
}
