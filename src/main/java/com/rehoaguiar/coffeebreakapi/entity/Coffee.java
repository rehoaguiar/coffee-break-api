package com.rehoaguiar.coffeebreakapi.entity;

import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coffee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name="name", unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String type;

    private String brand;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="roast_level", nullable = false)
    private RoastLevel roastLevel;

    private String flavorNotes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;

    @Column(name="description", length = 500)
    private String description;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
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
