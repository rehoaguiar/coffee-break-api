package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import com.rehoaguiar.coffeebreakapi.dto.TokenResponse;
import com.rehoaguiar.coffeebreakapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para cadastro e login de usuários.")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Cadastrar usuário", description = "Cria um usuário e retorna um token JWT.")
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Login", description = "Autentica um usuário cadastrado e retorna um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
