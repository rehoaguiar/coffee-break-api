package com.rehoaguiar.coffeebreakapi.entity;

import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/** Avaliação de um café no caderninho de uma pessoa. Cada conta tem as suas. */
@Entity
@Table(name = "coffee_review", uniqueConstraints = {
        @UniqueConstraint(name = "uk_review_user_name", columnNames = {"user_id", "name"}),
        @UniqueConstraint(name = "uk_review_user_key", columnNames = {"user_id", "client_key"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Chave usada pelo app (id do catálogo ou do café próprio). */
    @Column(name = "client_key", length = 120)
    private String clientKey;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String type;

    private String brand;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "roast_level", nullable = false)
    private RoastLevel roastLevel;

    private String flavorNotes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;

    @Column(name = "description", length = 500)
    private String description;

    /** Item do catálogo do app, se for um. */
    @Column(name = "item_id", length = 120)
    private String itemId;

    @Column(length = 20)
    private String color;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
