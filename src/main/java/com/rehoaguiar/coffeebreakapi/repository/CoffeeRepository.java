package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    boolean existsByUserAndNameIgnoreCase(User user, String name);

    boolean existsByUserAndNameIgnoreCaseAndIdNot(User user, String name, Long id);

    List<Coffee> findByUserOrderByCreatedAtDesc(User user);

    List<Coffee> findByUserAndTypeIgnoreCaseOrderByCreatedAtDesc(User user, String type);

    Optional<Coffee> findByIdAndUser(Long id, User user);

    Optional<Coffee> findByUserAndClientKey(User user, String clientKey);

    Optional<Coffee> findFirstByUserAndNameIgnoreCase(User user, String name);
}
