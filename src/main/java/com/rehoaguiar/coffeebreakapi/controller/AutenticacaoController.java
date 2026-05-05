package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import com.rehoaguiar.coffeebreakapi.dto.TokenResponse;
import com.rehoaguiar.coffeebreakapi.service.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<TokenResponse> cadastrar(@Valid @RequestBody RegisterRequest request) {
        TokenResponse response = autenticacaoService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = autenticacaoService.login(request);
        return ResponseEntity.ok(response);
    }
}
