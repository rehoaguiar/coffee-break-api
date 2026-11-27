package com.rehoaguiar.coffeebreakapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newAccountStartsEmpty() throws Exception {
        String token = register("Nova Pessoa");

        mockMvc.perform(get("/me/data").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.name").value("Nova Pessoa"))
                .andExpect(jsonPath("$.profile.refMg").value(400))
                .andExpect(jsonPath("$.profile.cutoff").value(18))
                .andExpect(jsonPath("$.profile.username").isNotEmpty())
                .andExpect(jsonPath("$.entries").isEmpty())
                .andExpect(jsonPath("$.reviews").isEmpty())
                .andExpect(jsonPath("$.mine").isEmpty())
                .andExpect(jsonPath("$.wish").isEmpty());
    }

    @Test
    void personalDataRequiresToken() throws Exception {
        mockMvc.perform(get("/me/data")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/coffees")).andExpect(status().isUnauthorized());
    }

    @Test
    void entriesAreIdempotentAndPrivatePerAccount() throws Exception {
        String ana = register("Ana");
        String bia = register("Bia");
        Map<String, Object> cup = Map.of("ts", 1_700_000_000_000L, "name", "Baggio Bourbon", "mg", 135, "ml", 250, "grams", 15, "itemId", "baggio-bourbon-grao");

        for (int i = 0; i < 2; i++) {
            mockMvc.perform(put("/me/entries/abc123").header(HttpHeaders.AUTHORIZATION, bearer(ana))
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cup)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("abc123"));
        }

        mockMvc.perform(get("/me/entries").header(HttpHeaders.AUTHORIZATION, bearer(ana)))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].mg").value(135))
                .andExpect(jsonPath("$[0].ml").value(250))
                .andExpect(jsonPath("$[0].grams").value(15));
        mockMvc.perform(get("/me/entries").header(HttpHeaders.AUTHORIZATION, bearer(bia)))
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(delete("/me/entries/abc123").header(HttpHeaders.AUTHORIZATION, bearer(ana)))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/me/entries").header(HttpHeaders.AUTHORIZATION, bearer(ana)))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void reviewsArePerAccountAndUpsertByKey() throws Exception {
        String ana = register("Ana");
        String bia = register("Bia");
        Map<String, Object> review = new java.util.HashMap<>(Map.of(
                "name", "Volluto", "type", "Cápsula", "roastLevel", "CLARA", "rating", "QUATRO_ESTRELAS", "itemId", "volluto"));

        // Mesmo nome em contas diferentes não conflita.
        for (String t : List.of(ana, bia)) {
            mockMvc.perform(put("/coffees/by-key/volluto").header(HttpHeaders.AUTHORIZATION, bearer(t))
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(review)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("volluto"));
        }

        review.put("rating", "CINCO_ESTRELAS");
        mockMvc.perform(put("/coffees/by-key/volluto").header(HttpHeaders.AUTHORIZATION, bearer(ana))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/coffees").header(HttpHeaders.AUTHORIZATION, bearer(ana)))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].rating").value("CINCO_ESTRELAS"));
        mockMvc.perform(get("/coffees").header(HttpHeaders.AUTHORIZATION, bearer(bia)))
                .andExpect(jsonPath("$[0].rating").value("QUATRO_ESTRELAS"));
    }

    @Test
    void updateProfileAndRejectTakenUsername() throws Exception {
        String ana = register("Ana");
        String bia = register("Bia");
        String handle = "ana" + UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(put("/me").header(HttpHeaders.AUTHORIZATION, bearer(ana))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("username", handle, "refMg", 300, "tastes", List.of("chocolate", "caramelo")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(handle))
                .andExpect(jsonPath("$.refMg").value(300))
                .andExpect(jsonPath("$.tastes[1]").value("caramelo"));

        mockMvc.perform(put("/me").header(HttpHeaders.AUTHORIZATION, bearer(bia))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("username", handle))))
                .andExpect(status().isConflict());
    }

    @Test
    void ownCoffeesAndWishlist() throws Exception {
        String t = register("Ana");
        mockMvc.perform(put("/me/own-coffees/o1").header(HttpHeaders.AUTHORIZATION, bearer(t))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "Coado da copa", "kind", "Coado", "mg", 120, "roast", "MEDIA"))))
                .andExpect(status().isOk());
        mockMvc.perform(put("/me/wishlist/kazaar").header(HttpHeaders.AUTHORIZATION, bearer(t)))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/me/wishlist/kazaar").header(HttpHeaders.AUTHORIZATION, bearer(t)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/me/data").header(HttpHeaders.AUTHORIZATION, bearer(t)))
                .andExpect(jsonPath("$.mine[0].id").value("o1"))
                .andExpect(jsonPath("$.wish.length()").value(1));
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    private String register(String name) throws Exception {
        RegisterRequest r = new RegisterRequest();
        r.setName(name);
        r.setEmail("me-" + UUID.randomUUID() + "@mail.com");
        r.setPassword("123456");
        String body = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("token").asText();
    }
}
