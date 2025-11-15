package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Query to fetch orders by userId
    List<Order> findByUserId(Long userId);

    // Add logging to the custom query method
    static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    default List<Order> findOrdersByUserId(Long userId) {
        logger.info("Fetching orders for user ID: {}", userId);
        List<Order> orders = findByUserId(userId);
        if (orders.isEmpty()) {
            logger.warn("No orders found for user ID: {}", userId);
        }
        return orders;
    }
}
