package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Custom query to find category by name
    Optional<Category> findByName(String name);

    // Add logging to your custom queries if needed
    static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

    default Optional<Category> findCategoryByName(String name) {
        logger.info("Searching category by name: {}", name);
        Optional<Category> category = findByName(name);
        if (!category.isPresent()) {
            logger.warn("Category with name '{}' not found", name);
        }
        return category;
    }
}
