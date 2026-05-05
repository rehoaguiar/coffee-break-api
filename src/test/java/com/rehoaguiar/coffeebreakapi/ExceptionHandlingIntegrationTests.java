package com.rehoaguiar.coffeebreakapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionHandlingIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnMethodNotAllowedWhenHttpMethodIsInvalidForEndpoint() throws Exception {
        mockMvc.perform(delete("/auth/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").value("Método HTTP não permitido para este endpoint"))
                .andExpect(jsonPath("$.path").value("/auth/register"));
    }
}
