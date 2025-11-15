package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Query to find items by name with case-insensitive matching
    List<Item> findByNameContainingIgnoreCase(String name);

    // Query to filter items by price range
    List<Item> findByPriceBetween(double minPrice, double maxPrice);

    // Custom query to find items by category name
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.name = :category")
    List<Item> findByCategoryName(@Param("category") String category);

    // Query to find all items by IDs
    Set<Item> findAllById(Set<Long> ids);

    // Add logging to your custom methods for better traceability
    static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

    default List<Item> findItemsByCategory(String category) {
        logger.info("Fetching items for category: {}", category);
        List<Item> items = findByCategoryName(category);
        if (items.isEmpty()) {
            logger.warn("No items found for category '{}'", category);
        }
        return items;
    }

    default List<Item> findItemsByPriceRange(double minPrice, double maxPrice) {
        logger.info("Fetching items in price range: {} - {}", minPrice, maxPrice);
        List<Item> items = findByPriceBetween(minPrice, maxPrice);
        if (items.isEmpty()) {
            logger.warn("No items found in price range: {} - {}", minPrice, maxPrice);
        }
        return items;
    }
}
