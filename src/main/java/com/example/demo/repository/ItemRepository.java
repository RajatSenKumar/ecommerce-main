package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByPriceBetween(double minPrice, double maxPrice);

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.name = :category")
    List<Item> findByCategoryName(@Param("category") String category);

}
