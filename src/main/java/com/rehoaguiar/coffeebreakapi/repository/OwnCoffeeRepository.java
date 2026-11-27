package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.OwnCoffee;
import com.rehoaguiar.coffeebreakapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnCoffeeRepository extends JpaRepository<OwnCoffee, Long> {
    List<OwnCoffee> findByUserOrderByIdAsc(User user);

    Optional<OwnCoffee> findByUserAndClientId(User user, String clientId);
}
