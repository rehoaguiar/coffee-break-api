package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.dto.CoffeeRequest;
import com.rehoaguiar.coffeebreakapi.dto.CoffeeResponse;
import com.rehoaguiar.coffeebreakapi.entity.Coffee;
import com.rehoaguiar.coffeebreakapi.service.CoffeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffees")
@RequiredArgsConstructor
@Tag(name = "Cafés", description = "Endpoints para cadastrar, listar, atualizar e remover cafés.")
public class CoffeeController {
    private final CoffeeService coffeeService;

    @Operation(summary = "Cadastrar café", description = "Cria um novo café.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<CoffeeResponse> create(@Valid @RequestBody CoffeeRequest request) {
        Coffee createdCoffee = coffeeService.create(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CoffeeResponse.fromEntity(createdCoffee));
    }

    @Operation(summary = "Listar cafés", description = "Retorna todos os cafés cadastrados.")
    @GetMapping
    public ResponseEntity<List<CoffeeResponse>> findAll() {
        List<Coffee> coffees = coffeeService.findAll();
        List<CoffeeResponse> response = coffees.stream()
                .map(CoffeeResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar café por id", description = "Retorna um café pelo id informado.")
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeResponse> findById(@PathVariable Long id) {
        Coffee coffee = coffeeService.findById(id);
        return ResponseEntity.ok(CoffeeResponse.fromEntity(coffee));
    }

    @Operation(summary = "Atualizar café", description = "Atualiza um café existente.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<CoffeeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeRequest request
    ) {
        Coffee updatedCoffee = coffeeService.update(id, request.toEntity());
        return ResponseEntity.ok(CoffeeResponse.fromEntity(updatedCoffee));
    }

    @Operation(summary = "Remover café", description = "Remove um café pelo id informado.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coffeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
