package com.thembu.guessza.clientconfig;

import com.thembu.guessza.config.JwtUtil;
import com.thembu.guessza.config.security.SecurityConfig;
import com.thembu.guessza.user.UserRepository;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.thembu.guessza.config.clientconfig.ConfigController;
import com.thembu.guessza.config.clientconfig.MapsProperties;
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
@Import(SecurityConfig.class) // run through the real security rules, not Boot's defaults
@WithMockUser               // these endpoints all require a logged-in user
class ConfigControllerTest {

    // SecurityConfig and JwtFilter need these, and the web slice doesn't load them
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsConfiguredApiKey() throws Exception {
        mockMvc.perform(get("/api/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiKey").value("test-key"));
    }
}