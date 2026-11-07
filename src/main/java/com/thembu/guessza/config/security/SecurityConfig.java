package com.thembu.guessza.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // spring security filter chain inspects request and makes decision on whether to block or let it go through based on config set up
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception {
        return  http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/api/auth/**").permitAll(); //auth requests do not need to be authenticated
                            authorizeHttp.anyRequest().authenticated(); //all other requests do
                        }
                ).csrf(Customizer.withDefaults()) //enable csrf attack protection
                .build();
    }

}
