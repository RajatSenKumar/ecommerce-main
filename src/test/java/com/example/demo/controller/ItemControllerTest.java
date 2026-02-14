package com.example.demo.controller;

import com.example.demo.dto.ItemRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllItems() throws Exception {
        Mockito.when(itemService.getAllItems()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateItem() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setName("Test");
        request.setPrice(100.0);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test");
        item.setPrice(100.0);

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Category");
        item.getCategories().add(cat);

        Mockito.when(itemService.createItem(any(ItemRequest.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.categories[0].name").value("Category"));
    }
}
