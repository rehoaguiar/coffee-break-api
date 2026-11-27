package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.dto.MeDtos.*;
import com.rehoaguiar.coffeebreakapi.service.MeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Minha conta", description = "Perfil, xícaras do dia, meus cafés e lista de desejos da conta logada.")
public class MeController {
    private final MeService meService;

    @Operation(summary = "Tudo da conta", description = "Perfil, xícaras, avaliações, meus cafés e lista de desejos numa chamada só.")
    @GetMapping("/data")
    public MeDataResponse all() {
        return meService.all();
    }

    @Operation(summary = "Ver perfil")
    @GetMapping
    public ProfileResponse profile() {
        return meService.profile();
    }

    @Operation(summary = "Atualizar perfil", description = "Atualização parcial: só os campos enviados mudam.")
    @PutMapping
    public ProfileResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return meService.updateProfile(request);
    }

    @Operation(summary = "Listar xícaras", description = "Filtro opcional por período (ms desde 1970).")
    @GetMapping("/entries")
    public List<EntryResponse> entries(@RequestParam(required = false) Long from, @RequestParam(required = false) Long to) {
        return meService.entries(from, to);
    }

    @Operation(summary = "Registrar ou atualizar xícara", description = "Idempotente pelo id gerado no app.")
    @PutMapping("/entries/{id}")
    public EntryResponse putEntry(@PathVariable @Size(max = 64) String id, @Valid @RequestBody EntryRequest request) {
        return meService.putEntry(id, request);
    }

    @Operation(summary = "Remover xícara")
    @DeleteMapping("/entries/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        meService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar meus cafés")
    @GetMapping("/own-coffees")
    public List<OwnCoffeeResponse> ownCoffees() {
        return meService.ownCoffees();
    }

    @Operation(summary = "Cadastrar ou atualizar café próprio", description = "Idempotente pelo id gerado no app.")
    @PutMapping("/own-coffees/{id}")
    public OwnCoffeeResponse putOwnCoffee(@PathVariable @Size(max = 64) String id, @Valid @RequestBody OwnCoffeeRequest request) {
        return meService.putOwnCoffee(id, request);
    }

    @Operation(summary = "Remover café próprio")
    @DeleteMapping("/own-coffees/{id}")
    public ResponseEntity<Void> deleteOwnCoffee(@PathVariable String id) {
        meService.deleteOwnCoffee(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar lista de desejos")
    @GetMapping("/wishlist")
    public List<String> wishlist() {
        return meService.wishlist();
    }

    @Operation(summary = "Adicionar à lista de desejos")
    @PutMapping("/wishlist/{itemId}")
    public ResponseEntity<Void> addWish(@PathVariable @Size(max = 120) String itemId) {
        meService.addWish(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remover da lista de desejos")
    @DeleteMapping("/wishlist/{itemId}")
    public ResponseEntity<Void> removeWish(@PathVariable String itemId) {
        meService.removeWish(itemId);
        return ResponseEntity.noContent().build();
    }
}
