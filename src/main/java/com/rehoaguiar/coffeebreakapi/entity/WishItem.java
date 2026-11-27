package com.rehoaguiar.coffeebreakapi.entity;

import jakarta.persistence.*;
import lombok.*;

/** Café do catálogo que a pessoa quer provar. */
@Entity
@Table(name = "wish_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_wish_user_item", columnNames = {"user_id", "item_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "item_id", nullable = false, length = 120)
    private String itemId;
}
