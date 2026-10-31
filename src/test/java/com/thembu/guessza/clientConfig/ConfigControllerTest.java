package com.thembu.guessza.clientConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableConfigurationProperties(MapsProperties.class)
@WebMvcTest(controllers = ConfigController.class, properties = "guessza.maps.api-key=test-key")
class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsConfiguredApiKey() throws Exception {
        mockMvc.perform(get("/api/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiKey").value("test-key"));
    }
}