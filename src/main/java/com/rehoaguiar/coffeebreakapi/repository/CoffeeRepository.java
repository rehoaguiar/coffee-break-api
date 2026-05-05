package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
