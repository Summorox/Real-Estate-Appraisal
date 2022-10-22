package pt.ipp.isep.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ipp.isep.model.Item;
import pt.ipp.isep.service.ItemService;

import java.util.List;


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

    @PutMapping
    public ResponseEntity<Void> update(Item item) {
        service.update(item);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Item>> findAllByGroupId(@RequestParam  Integer groupId) {
        return ResponseEntity.ok(service.findAllByGroupId(groupId));
    }
}
