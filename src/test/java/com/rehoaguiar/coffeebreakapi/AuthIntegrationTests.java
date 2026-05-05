package com.rehoaguiar.coffeebreakapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginShouldReturnToken() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@mail.com";

        RegisterRequest register = new RegisterRequest();
        register.setName("Test User");
        register.setEmail(email);
        register.setPassword("123456");

        mockMvc.perform(post("/auth/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void createCoffeeShouldRequireJwt() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Cafe Teste");
        payload.put("type", "ESPRESSO");
        payload.put("brand", "Marca X");
        payload.put("roastLevel", "MEDIA");
        payload.put("flavorNotes", "Chocolate");
        payload.put("rating", "QUATRO_ESTRELAS");
        payload.put("description", "Teste");

        mockMvc.perform(post("/coffees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }
}
