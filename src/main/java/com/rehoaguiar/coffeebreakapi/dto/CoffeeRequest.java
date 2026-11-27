package com.rehoaguiar.coffeebreakapi.dto;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoffeeRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String type;

    @Size(max = 255)
    private String brand;

    @NotNull
    private RoastLevel roastLevel;

    @Size(max = 255)
    private String flavorNotes;

    @NotNull
    private Rating rating;

    @Size(max = 500)
    private String description;

    /** Id do item no catálogo do app (opcional). */
    @Size(max = 120)
    private String itemId;

    /** Cor usada pelo app para desenhar o café (opcional). */
    @Size(max = 20)
    private String color;

    public Coffee toEntity() {
        return Coffee.builder()
                .name(name.trim())
                .type(type)
                .brand(brand)
                .roastLevel(roastLevel)
                .flavorNotes(flavorNotes)
                .rating(rating)
                .description(description)
                .itemId(itemId)
                .color(color)
                .build();
    }
}
