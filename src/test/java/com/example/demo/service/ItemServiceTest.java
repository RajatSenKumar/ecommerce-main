package com.example.demo.service;

import com.example.demo.dto.ItemRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(100.0);
        item.getCategories().add(category);
    }

    @Test
    void testGetItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Item found = itemService.getItem(1L);
        assertEquals("Test Item", found.getName());
        assertEquals(1, found.getCategories().size());
    }

    @Test
    void testGetItem_NotFound() {
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> itemService.getItem(2L));
    }

    @Test
    void testCreateItem() {
        ItemRequest request = new ItemRequest();
        request.setName("New Item");
        request.setPrice(50.0);
        Item savedItem = new Item();
        savedItem.setId(2L);
        savedItem.setName(request.getName());
        savedItem.setPrice(request.getPrice());

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Category");
        savedItem.getCategories().add(cat);

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        Item result = itemService.createItem(request);
        assertEquals("New Item", result.getName());
        assertEquals(1, result.getCategories().size());
        assertEquals("Category", result.getCategories().iterator().next().getName());
    }
}
