package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;

    // Create a new order
    public Order createOrder(OrderRequest request) {
        logger.info("Creating new order for user ID: {}", request.getUserId());

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty. Cannot place order.");
        }

        // Copy items before clearing
        Set<Item> items = new HashSet<>(cart.getItems());

        // Clear cart
        cart.getItems().clear();

        // Save cart
        cartRepo.save(cart);

        if (items.isEmpty()) {
            logger.error("No valid items found for the order");
            throw new ResourceNotFoundException("No valid items found for the order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepo.save(order);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepo.findAll();
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
                    return new ResourceNotFoundException("Order not found");
                });
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        logger.info("Updating status of order ID: {} to {}", orderId, status);

        // Validate status
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }

        Order order = getOrder(orderId);
        try {
            order.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid status. Allowed values: PENDING, SHIPPED, DELIVERED, CANCELLED"
            );
        }
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepo.save(order);
    }

    // Cancel an order
    public void cancelOrder(Long orderId) {
        logger.info("Canceling order with ID: {}", orderId);

        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        logger.info("Order with ID: {} has been cancelled", orderId);
    }
}