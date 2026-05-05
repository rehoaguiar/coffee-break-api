package com.rehoaguiar.coffeebreakapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StatusResponse {
    private String status;
    private String application;
    private String version;
    private String message;
    private LocalDateTime timestamp;
}
