package com.rehoaguiar.coffeebreakapi.entity;

import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.persistence.*;
import lombok.*;

/** Café cadastrado pela própria pessoa (Meus cafés). */
@Entity
@Table(name = "own_coffee",
        uniqueConstraints = @UniqueConstraint(name = "uk_own_user_client", columnNames = {"user_id", "client_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "client_id", nullable = false, length = 64)
    private String clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String kind;

    @Column(nullable = false)
    private Integer mg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoastLevel roast;

    @Column(length = 20)
    private String color;
}
