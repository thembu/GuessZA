package com.thembu.guessza.config.security;


import com.thembu.guessza.config.JwtFilter;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import com.thembu.guessza.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // spring security filter chain inspects request and makes decision on whether to block or let it go through based on config set up
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http ,  JwtFilter jwtFilter) throws  Exception {
        return  http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/api/auth/**" , "/error").permitAll(); //auth requests do not need to be authenticated
                            authorizeHttp.anyRequest().authenticated(); //all other requests do
                            authorizeHttp.requestMatchers("/", "/*.html", "/js/**", "/images/**", "/*.mp3").permitAll();
                        }
                ).csrf(csrf -> csrf.disable()) //disable csrf attack protection
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }



    //automatically wires together password encoder and userdetails bean so they can be used in auth
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //used to encode our passwords
    @Bean
    public PasswordEncoder passwordEncoder () {
        return  new BCryptPasswordEncoder();
    }


    //springs way of extracting user info like username and password to be passed on to authmanager so it can use it in log in route
    @Bean
    public  UserDetailsService userDetailsService(UserRepository userRepository) {
        return  new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

                return org.springframework.security.core.userdetails.User.
                        withUsername(user.getUsername())
                        .password(user.getPassword())
                        .build();
            }
        };
    }


}
