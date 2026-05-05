package com.rehoaguiar.coffeebreakapi.controller;

import com.rehoaguiar.coffeebreakapi.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Status", description = "Endpoint para consultar o funcionamento da API.")
public class StatusController {

    @Operation(summary = "Consultar status", description = "Retorna informações básicas sobre o funcionamento da API.")
    @GetMapping("/status")
    public ResponseEntity<StatusResponse> status() {
        StatusResponse response = StatusResponse.builder()
                .status("UP")
                .application("Coffee Break API")
                .version("v1")
                .message("API em funcionamento")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
