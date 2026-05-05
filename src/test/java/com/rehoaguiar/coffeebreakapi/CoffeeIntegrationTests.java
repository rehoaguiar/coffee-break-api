package com.rehoaguiar.coffeebreakapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoffeeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCoffeeShouldReturnConflictWhenNameAlreadyExists() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> payload = coffeePayload("Cafe Duplicado " + UUID.randomUUID());

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe um cafe cadastrado com esse nome"));
    }

    @Test
    void updateCoffeeShouldReturnConflictWhenNameAlreadyExistsInAnotherCoffee() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> firstCoffee = coffeePayload("Cafe Original " + UUID.randomUUID());
        Map<String, Object> secondCoffee = coffeePayload("Cafe Para Atualizar " + UUID.randomUUID());

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstCoffee)))
                .andExpect(status().isCreated());

        String secondResponse = mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondCoffee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode secondJson = objectMapper.readTree(secondResponse);
        Long secondCoffeeId = secondJson.get("id").asLong();
        secondCoffee.put("name", firstCoffee.get("name"));

        mockMvc.perform(put("/coffees/" + secondCoffeeId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondCoffee)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe um cafe cadastrado com esse nome"));
    }

    @Test
    void createCoffeeShouldReturnBadRequestWhenEnumIsInvalid() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> payload = coffeePayload("Cafe Enum Invalido " + UUID.randomUUID());
        payload.put("roastLevel", "TORRA_INVALIDA");

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Formato da requisicao invalido"));
    }

    private String registerAndGetToken() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@mail.com";

        RegisterRequest register = new RegisterRequest();
        register.setName("Test User");
        register.setEmail(email);
        register.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("123456");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private Map<String, Object> coffeePayload(String name) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("type", "ARABICA");
        payload.put("brand", "NESPRESSO");
        payload.put("roastLevel", "MEDIA");
        payload.put("flavorNotes", "Chocolate e caramelo");
        payload.put("rating", "QUATRO_ESTRELAS");
        payload.put("description", "Cafe criado em teste de integracao");
        return payload;
    }
}
