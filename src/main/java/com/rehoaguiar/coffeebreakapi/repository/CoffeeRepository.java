package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Coffee> findByTypeIgnoreCase(String type);
}
