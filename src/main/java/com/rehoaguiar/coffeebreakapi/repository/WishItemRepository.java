package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.entity.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    List<WishItem> findByUserOrderByIdAsc(User user);

    Optional<WishItem> findByUserAndItemId(User user, String itemId);
}
