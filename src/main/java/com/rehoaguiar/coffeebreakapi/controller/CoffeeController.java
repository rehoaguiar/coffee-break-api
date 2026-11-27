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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cafés", description = "Avaliações do caderninho da conta logada.")
public class CoffeeController {
    private final CoffeeService coffeeService;

    @Operation(summary = "Cadastrar avaliação", description = "Cria uma avaliação na conta logada.")
    @PostMapping
    public ResponseEntity<CoffeeResponse> create(@Valid @RequestBody CoffeeRequest request) {
        Coffee created = coffeeService.create(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CoffeeResponse.fromEntity(created));
    }

    @Operation(summary = "Listar avaliações", description = "Retorna as avaliações da conta logada, com filtro opcional por tipo.")
    @GetMapping
    public ResponseEntity<List<CoffeeResponse>> findAll(@RequestParam(required = false) String type) {
        List<Coffee> coffees = type == null || type.isBlank() ? coffeeService.findAll() : coffeeService.findByType(type);
        return ResponseEntity.ok(coffees.stream().map(CoffeeResponse::fromEntity).toList());
    }

    @Operation(summary = "Buscar avaliação por id")
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(CoffeeResponse.fromEntity(coffeeService.findById(id)));
    }

    @Operation(summary = "Atualizar avaliação")
    @PutMapping("/{id}")
    public ResponseEntity<CoffeeResponse> update(@PathVariable Long id, @Valid @RequestBody CoffeeRequest request) {
        return ResponseEntity.ok(CoffeeResponse.fromEntity(coffeeService.update(id, request.toEntity())));
    }

    @Operation(summary = "Criar ou atualizar pela chave do app", description = "Idempotente: usado pelo app para salvar e sincronizar.")
    @PutMapping("/by-key/{key}")
    public ResponseEntity<CoffeeResponse> upsertByKey(@PathVariable String key, @Valid @RequestBody CoffeeRequest request) {
        return ResponseEntity.ok(CoffeeResponse.fromEntity(coffeeService.upsertByKey(key, request.toEntity())));
    }

    @Operation(summary = "Remover avaliação")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coffeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remover avaliação pela chave do app")
    @DeleteMapping("/by-key/{key}")
    public ResponseEntity<Void> deleteByKey(@PathVariable String key) {
        coffeeService.deleteByKey(key);
        return ResponseEntity.noContent().build();
    }
}
