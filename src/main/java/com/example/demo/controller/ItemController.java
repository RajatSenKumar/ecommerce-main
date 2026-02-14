package com.example.demo.controller;

import com.example.demo.dto.ItemRequest;
import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieve a list of all items")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<List<Item>> getAllItems() {
        logger.info("Fetching all items");
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieve a specific item by its ID")
    @ApiResponse(responseCode = "200", description = "Item retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public ResponseEntity<Item> getItem(@PathVariable @Positive Long id) {
        logger.info("Fetching item with ID: {}", id);
        Item item = itemService.getItem(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    @Operation(summary = "Create a new item", description = "Add a new item to the inventory")
    @ApiResponse(responseCode = "201", description = "Item created successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    public ResponseEntity<Item> createItem(@RequestBody @Valid ItemRequest request) {
        logger.info("Creating a new item");
        Item item = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item", description = "Update an existing item's details")
    @ApiResponse(responseCode = "200", description = "Item updated successfully")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public ResponseEntity<Item> updateItem(@PathVariable @Positive Long id, @RequestBody @Valid ItemRequest request) {
        logger.info("Updating item with ID: {}", id);
        Item item = itemService.updateItem(id, request);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item", description = "Remove an item by its ID")
    @ApiResponse(responseCode = "200", description = "Item deleted successfully")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public ResponseEntity<Void> deleteItem(@PathVariable @Positive Long id) {
        logger.info("Deleting item with ID: {}", id);
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search items by name", description = "Retrieve items containing the specified name")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<List<Item>> searchByName(@RequestParam @NotBlank String name) {
        logger.info("Searching items by name: {}", name);
        List<Item> items = itemService.searchByName(name);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter/price")
    @Operation(summary = "Filter items by price range", description = "Retrieve items within the given price range")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<List<Item>> filterByPrice(@RequestParam @Positive double min, @RequestParam @Positive double max) {
        logger.info("Filtering items by price range: {} - {}", min, max);
        List<Item> items = itemService.filterByPrice(min, max);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter/category")
    @Operation(summary = "Filter items by category", description = "Retrieve items belonging to the specified category")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<List<Item>> filterByCategory(@RequestParam String category) {
        logger.info("Filtering items by category: {}", category);
        List<Item> items = itemService.filterByCategory(category);
        return ResponseEntity.ok(items);
    }
}