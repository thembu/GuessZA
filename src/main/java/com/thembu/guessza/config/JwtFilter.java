package com.thembu.guessza.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final  JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //extract auth header from request
        String authHeader = request.getHeader("Authorization");

        //check if bearer token is in header
        if(authHeader != null && authHeader.startsWith("Bearer ")) {

            //get token from header
            String token = authHeader.substring(7);

            //check that the jwt token is valid (not expired , or tempered with)
             if(jwtUtil.isTokenValid(token)) {

                 try {
                     //extract username and use it to load user from user details service
                     String username = jwtUtil.extractUsername(token);
                     UserDetails user = userDetailsService.loadUserByUsername(username);


                     // Token is valid: mark this request as authenticated.
                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, null);
                     //attach request info t for logging e.g(ip , session id)
                     authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                     //mark as logged in
                     SecurityContextHolder.getContext().setAuthentication(authToken);

                 }catch (UsernameNotFoundException e) {
                 }

             }
        }

        filterChain.doFilter(request, response);

    }
}
