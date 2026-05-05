package com.rehoaguiar.coffeebreakapi.dto;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CoffeeResponse {
    private Long id;
    private String name;
    private String type;
    private String brand;
    private RoastLevel roastLevel;
    private String flavorNotes;
    private Rating rating;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CoffeeResponse fromEntity(Coffee coffee) {
        return CoffeeResponse.builder()
                .id(coffee.getId())
                .name(coffee.getName())
                .type(coffee.getType())
                .brand(coffee.getBrand())
                .roastLevel(coffee.getRoastLevel())
                .flavorNotes(coffee.getFlavorNotes())
                .rating(coffee.getRating())
                .description(coffee.getDescription())
                .createdAt(coffee.getCreatedAt())
                .updatedAt(coffee.getUpdatedAt())
                .build();
    }
}
