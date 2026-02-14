package com.example.demo.integration;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndFindOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);

        Order saved = orderRepository.save(order);
        assertNotNull(saved.getId());

        Order found = orderRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(OrderStatus.PENDING, found.getStatus());
    }
}
