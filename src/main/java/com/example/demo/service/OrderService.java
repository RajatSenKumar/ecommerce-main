package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepo;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    // Create a new order
    public Order createOrder(OrderRequest request) {
        logger.info("Creating new order for user ID: {}", request.getUserId());

        // Validate the request
        if (request.getUserId() == null || request.getItemIds() == null || request.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("User ID and Item IDs are required");
        }

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Item> items = itemRepo.findAllById(request.getItemIds());
        if (items.isEmpty()) {
            logger.error("No valid items found for the order");
            throw new RuntimeException("No valid items found for the order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        logger.info("Order created successfully with ID: {}", order.getId());
        return orderRepo.save(order);
    }

    // Get orders by user ID
    public List<Order> getOrdersByUser(Long userId) {
        logger.info("Fetching orders for user ID: {}", userId);
        return orderRepo.findByUserId(userId);
    }

    // Get a single order by ID
    public Order getOrder(Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        return orderRepo.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order with ID {} not found", orderId);
                    return new RuntimeException("Order not found");
                });
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, String status) {
        logger.info("Updating status of order ID: {} to {}", orderId, status);

        // Validate status
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        Order order = getOrder(orderId);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepo.save(order);
    }

    // Cancel an order
    public void cancelOrder(Long orderId) {
        logger.info("Canceling order with ID: {}", orderId);

        Order order = getOrder(orderId);
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        logger.info("Order with ID: {} has been cancelled", orderId);
    }
}