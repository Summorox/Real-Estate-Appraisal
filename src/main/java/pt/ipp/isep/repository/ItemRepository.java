package pt.ipp.isep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipp.isep.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByGroupId(Integer groupId);
}
