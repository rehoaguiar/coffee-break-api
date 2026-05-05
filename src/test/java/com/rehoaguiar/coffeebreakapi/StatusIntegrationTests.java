package com.rehoaguiar.coffeebreakapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatusIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusShouldReturnApiInformationWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("Coffee Break API"))
                .andExpect(jsonPath("$.version").value("v1"))
                .andExpect(jsonPath("$.message").value("API em funcionamento"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
