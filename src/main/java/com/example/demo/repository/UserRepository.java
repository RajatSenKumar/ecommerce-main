package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UserRepository extends JpaRepository<User, Long> {

    // Add logging to UserRepository
    static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    default Optional<User> findUserById(Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        Optional<User> user = findById(userId);
        if (!user.isPresent()) {
            logger.warn("User with ID '{}' not found", userId);
        }
        return user;
    }
}
