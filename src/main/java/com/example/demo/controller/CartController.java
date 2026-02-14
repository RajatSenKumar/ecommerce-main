package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user cart", description = "Retrieve the shopping cart for a specific user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public ResponseEntity<Cart> getCart(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }

    @PostMapping("/{userId}/add/{itemId}")
    @Operation(summary = "Add item to cart", description = "Add a specific item to a user's cart")
    @ApiResponse(responseCode = "200", description = "Item added to cart successfully")
    @ApiResponse(responseCode = "404", description = "User or item not found")
    public ResponseEntity<Cart> addItem(@PathVariable @Positive Long userId, @PathVariable @Positive Long itemId) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, itemId));
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from a user's cart")
    @ApiResponse(responseCode = "200", description = "Item removed from cart successfully")
    @ApiResponse(responseCode = "404", description = "User or item not found")
    public ResponseEntity<Cart> removeItem(@PathVariable @Positive Long userId, @PathVariable @Positive Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, itemId));
    }
}
