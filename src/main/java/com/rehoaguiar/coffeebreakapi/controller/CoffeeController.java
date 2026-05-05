package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.service.CoffeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffees")
@RequiredArgsConstructor
public class CoffeeController {
    private final CoffeeService coffeeService;

    @PostMapping
    public ResponseEntity<Coffee> create(@Valid @RequestBody Coffee coffee) {
        Coffee createdCoffee = coffeeService.create(coffee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoffee);
    }

    @GetMapping
    public ResponseEntity<List<Coffee>> findAll() {
        List<Coffee> coffees = coffeeService.findAll();
        return ResponseEntity.ok(coffees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coffee> findById(@PathVariable Long id) {
        Coffee coffee = coffeeService.findById(id);
        return ResponseEntity.ok(coffee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coffee> update(
            @PathVariable Long id,
            @Valid @RequestBody Coffee coffee
    ) {
        Coffee updatedCoffee = coffeeService.update(id, coffee);
        return ResponseEntity.ok(updatedCoffee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coffeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
