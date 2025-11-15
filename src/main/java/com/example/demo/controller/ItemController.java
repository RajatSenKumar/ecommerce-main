package com.example.demo.controller;

import com.example.demo.dto.ItemRequest;
import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        logger.info("Fetching all items");
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        logger.info("Fetching item with ID: {}", id);
        try {
            Item item = itemService.getItem(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            logger.error("Error fetching item with ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found", e);
        }
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody @Valid ItemRequest request, BindingResult result) {
        logger.info("Creating a new item");
        if (result.hasErrors()) {
            logger.error("Validation failed for item creation: {}", result.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        Item item = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request, BindingResult result) {
        logger.info("Updating item with ID: {}", id);
        if (result.hasErrors()) {
            logger.error("Validation failed for item update: {}", result.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            Item item = itemService.updateItem(id, request);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            logger.error("Error updating item with ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        logger.info("Deleting item with ID: {}", id);
        try {
            itemService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting item with ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found", e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchByName(@RequestParam String name) {
        logger.info("Searching items by name: {}", name);
        List<Item> items = itemService.searchByName(name);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<Item>> filterByPrice(@RequestParam double min, @RequestParam double max) {
        logger.info("Filtering items by price range: {} - {}", min, max);
        List<Item> items = itemService.filterByPrice(min, max);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter/category")
    public ResponseEntity<List<Item>> filterByCategory(@RequestParam String category) {
        logger.info("Filtering items by category: {}", category);
        List<Item> items = itemService.filterByCategory(category);
        return ResponseEntity.ok(items);
    }
}