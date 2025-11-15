package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import com.example.demo.exception.ResourceNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    // Get all categories
    public List<Category> getAllCategories() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            logger.warn("No categories found");
        }
        return categories;
    }

    // Get category by ID
    public Category getCategoryById(Long id) {
        if (id == null) {
            logger.error("Invalid ID provided: null");
            throw new IllegalArgumentException("Category ID cannot be null");
        }

        logger.info("Fetching category with ID: {}", id);
        return categoryRepository.findById(id).orElseThrow(() -> {
            logger.error("Category with ID {} not found", id);
            return new ResourceNotFoundException("Category not found");
        });
    }
}
