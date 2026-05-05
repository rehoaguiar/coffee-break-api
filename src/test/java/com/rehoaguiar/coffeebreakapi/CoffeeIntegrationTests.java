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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Map<String, Object> payload = coffeePayload("Café Duplicado " + UUID.randomUUID());

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
                .andExpect(jsonPath("$.message").value("Já existe um café cadastrado com esse nome"));
    }

    @Test
    void updateCoffeeShouldReturnConflictWhenNameAlreadyExistsInAnotherCoffee() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> firstCoffee = coffeePayload("Café Original " + UUID.randomUUID());
        Map<String, Object> secondCoffee = coffeePayload("Café Para Atualizar " + UUID.randomUUID());

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
                .andExpect(jsonPath("$.message").value("Já existe um café cadastrado com esse nome"));
    }

    @Test
    void createCoffeeShouldReturnBadRequestWhenEnumIsInvalid() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> payload = coffeePayload("Café Enum Inválido " + UUID.randomUUID());
        payload.put("roastLevel", "TORRA_INVALIDA");

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Formato da requisição inválido"));
    }

    @Test
    void findAllShouldFilterCoffeesByType() throws Exception {
        String token = registerAndGetToken();
        Map<String, Object> capsuleCoffee = coffeePayload("Café Cápsula " + UUID.randomUUID());
        capsuleCoffee.put("type", "CAPSULA");
        Map<String, Object> beansCoffee = coffeePayload("Café Grãos " + UUID.randomUUID());
        beansCoffee.put("type", "GRAOS");

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(capsuleCoffee)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/coffees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beansCoffee)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/coffees")
                        .param("type", "capsula"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(capsuleCoffee.get("name")))
                .andExpect(jsonPath("$[0].type").value("CAPSULA"))
                .andExpect(jsonPath("$[1]").doesNotExist());
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
        payload.put("description", "Café criado em teste de integração");
        return payload;
    }
}
