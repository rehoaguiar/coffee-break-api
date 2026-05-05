package com.rehoaguiar.coffeebreakapi.dto;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.enums.Rating;
import com.rehoaguiar.coffeebreakapi.enums.RoastLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoffeeRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private String brand;

    @NotNull
    private RoastLevel roastLevel;

    private String flavorNotes;

    @NotNull
    private Rating rating;

    private String description;

    public Coffee toEntity() {
        return Coffee.builder()
                .name(name)
                .type(type)
                .brand(brand)
                .roastLevel(roastLevel)
                .flavorNotes(flavorNotes)
                .rating(rating)
                .description(description)
                .build();
    }
}
