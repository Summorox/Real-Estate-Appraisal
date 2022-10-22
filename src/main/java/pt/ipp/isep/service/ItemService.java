package pt.ipp.isep.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ipp.isep.model.Item;
import pt.ipp.isep.repository.ItemRepository;

@AllArgsConstructor
@Service
public class ItemService {

    private ItemRepository repository;

    public void save(Item item) {
        repository.save(item);
    }

    public void update(Item item) {
        if (!repository.existsById(item.getId())) {
            //TODO Add APIException
        }
        repository.save(item);
    }
}
