package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface CoffeeRepository extends JpaRepository<Coffee, Long>, JpaSpecificationExecutor<Coffee> {
    List<Coffee> findByNameContainingIgnoreCase(String name);
    List<Coffee> findByType(String type);
    List<Coffee> findByRoastLevel(RoastLevel roastLevel);
    List<Coffee> findByRating(Rating rating);
}
