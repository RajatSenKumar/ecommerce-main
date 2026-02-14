package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    @Operation(summary = "Create a new order", description = "Place a new order for a user based on their cart items")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed or cart is empty")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequest request) {
        logger.info("Creating new order");
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponse(responseCode = "200", description = "Order retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Order> getOrder(@PathVariable @Positive Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user", description = "Retrieve all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable @Positive Long userId) {
        logger.info("Fetching orders for user with ID: {}", userId);
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update order status", description = "Update the status of an order (PENDING, SHIPPED, DELIVERED, CANCELLED)")
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable @Positive Long orderId, @RequestParam OrderStatus status) {
        logger.info("Updating status of order with ID: {} to {}", orderId, status);
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel order", description = "Cancel a specific order by its ID")
    @ApiResponse(responseCode = "204", description = "Order cancelled successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> cancelOrder(@PathVariable @Positive Long orderId) {
        logger.info("Canceling order with ID: {}", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}