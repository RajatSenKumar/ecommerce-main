package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order found = orderService.getOrder(1L);
        assertEquals(OrderStatus.PENDING, found.getStatus());
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrder(2L));
    }
}
