package com.example.demo.integration;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveAndFindItem() {
        // Create category
        Category category = new Category();
        category.setName("Test Category");

        // Create item
        Item item = new Item();
        item.setName("Integration Item");
        item.setPrice(150.0);
        item.getCategories().add(category);

        Item saved = itemRepository.save(item);
        assertNotNull(saved.getId());

        Item found = itemRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Integration Item", found.getName());
        assertEquals(1, found.getCategories().size());
    }
}
