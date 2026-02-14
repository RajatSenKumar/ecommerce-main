package com.example.demo.integration;

import com.example.demo.entity.Cart;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartRepositoryIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindCart() {
        User user = new User();
        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);

        Cart saved = cartRepository.save(cart);
        assertNotNull(saved.getId());

        Cart found = cartRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(user.getId(), found.getUser().getId());
    }
}
