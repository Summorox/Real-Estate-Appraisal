package pt.ipp.isep.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pt.ipp.isep.model.ApiException;
import pt.ipp.isep.model.Item;
import pt.ipp.isep.repository.ItemRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemService {

    private ItemRepository repository;

    public void save(Item item) {
        repository.save(item);
    }

    public void update(Item item) {
        if (!repository.existsById(item.getId())) {
            throw new ApiException("Not found the item", HttpStatus.NOT_FOUND);
        }
        repository.save(item);
    }

    public List<Item> findAllByGroupId(Integer groupId) {
        return repository.findAllByGroupId(groupId);
    }
}
