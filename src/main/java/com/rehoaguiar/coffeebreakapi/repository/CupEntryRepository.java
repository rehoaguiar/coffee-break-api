package com.rehoaguiar.coffeebreakapi.repository;

import com.rehoaguiar.coffeebreakapi.entity.CupEntry;
import com.rehoaguiar.coffeebreakapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CupEntryRepository extends JpaRepository<CupEntry, Long> {
    List<CupEntry> findByUserOrderByTsAsc(User user);

    List<CupEntry> findByUserAndTsBetweenOrderByTsAsc(User user, Long from, Long to);

    Optional<CupEntry> findByUserAndClientId(User user, String clientId);
}
