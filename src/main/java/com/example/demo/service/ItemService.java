package com.example.demo.service;

import com.example.demo.entity.Item;
import com.example.demo.entity.Category;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.dto.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepo;
    private final CategoryRepository categoryRepo;

    // Fetch all items
    public List<Item> getAllItems() {
        logger.info("Fetching all items");
        return itemRepo.findAll();
    }

    // Get a single item by ID
    public Item getItem(Long id) {
        logger.info("Fetching item with ID: {}", id);
        return itemRepo.findById(id).orElseThrow(() -> {
            logger.error("Item with ID {} not found", id);
            return new RuntimeException("Item not found");
        });
    }

    // Create a new item
    public Item createItem(ItemRequest request) {
        logger.info("Creating new item with name: {}", request.getName());

        // Validate request data
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategories(resolveCategories(request.getCategoryNames()));

        logger.info("Item created with name: {}", item.getName());
        return itemRepo.save(item);
    }

    // Update an existing item
    public Item updateItem(Long id, ItemRequest request) {
        logger.info("Updating item with ID: {}", id);

        Item item = getItem(id);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategories(resolveCategories(request.getCategoryNames()));

        logger.info("Item with ID {} updated", id);
        return itemRepo.save(item);
    }

    // Delete an item by ID
    public void deleteItem(Long id) {
        logger.info("Deleting item with ID: {}", id);
        try {
            itemRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Item with ID {} not found for deletion", id);
            throw new RuntimeException("Item not found");
        }
    }

    // Search items by name (case-insensitive)
    public List<Item> searchByName(String name) {
        logger.info("Searching items with name containing: {}", name);
        return itemRepo.findByNameContainingIgnoreCase(name);
    }

    // Filter items by price range
    public List<Item> filterByPrice(double min, double max) {
        logger.info("Filtering items by price range: {} - {}", min, max);
        return itemRepo.findByPriceBetween(min, max);
    }

    // Filter items by category
    public List<Item> filterByCategory(String category) {
        logger.info("Filtering items by category: {}", category);
        return itemRepo.findByCategoryName(category);
    }

    // Resolve categories based on the names
    private Set<Category> resolveCategories(Set<String> names) {
        logger.info("Resolving categories for names: {}", names);

        return names.stream()
                .map(name -> categoryRepo.findByName(name).orElseGet(() -> {
                    logger.info("Creating new category with name: {}", name);
                    return categoryRepo.save(new Category(null, name, null, null));
                }))
                .collect(Collectors.toSet());
    }
}