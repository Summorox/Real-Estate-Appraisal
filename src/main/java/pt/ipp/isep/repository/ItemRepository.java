package pt.ipp.isep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipp.isep.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
