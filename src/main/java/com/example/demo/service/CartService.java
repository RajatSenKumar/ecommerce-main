package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;

    public Cart getCartByUser(Long userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    public Cart addItemToCart(Long userId, Long itemId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                });

        cart.getItems().add(item);

        logger.info("Item added to cart for user {}", userId);

        return cartRepo.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long itemId) {

        Cart cart = getCartByUser(userId);

        cart.getItems().removeIf(item -> item.getId().equals(itemId));

        logger.info("Item removed from cart for user {}", userId);

        return cartRepo.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUser(userId);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
