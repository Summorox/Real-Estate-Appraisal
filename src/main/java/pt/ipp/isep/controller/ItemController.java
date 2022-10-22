package pt.ipp.isep.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ipp.isep.model.Item;
import pt.ipp.isep.service.ItemService;


@AllArgsConstructor
@RestController
@RequestMapping("item")
public class ItemController {

    private ItemService service;

    @PostMapping
    public ResponseEntity<Void> create(Item item) {
        service.save(item);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> update(Item item) {
        service.update(item);
        return ResponseEntity.noContent().build();
    }
}
