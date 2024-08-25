package com.Ashish.ProjectManagementSystem.AppConfig;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import java.util.Collections;
import java.util.List;

//This is configuration class
//We enable the web security

@Configuration
@EnableWebSecurity
public class AppConfig {

//    We make this as a bean so that we can use it
    @Bean

//    We will implement the spring security here....
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//        this is for session management
        http.sessionManagement(Management -> Management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //we are giving access
                .authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
//                this is regarding the jwt token
                .addFilterBefore(new JwtTokenValidator() , BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
//                this we use because when we  try to communicate with the client side then it will help
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                creating the cors configuration
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList(
//                        this is  for React
                        "http://localhost:3000/",
//                        this is for vit
                        "http://localhost:5173/",
//                        this is for angular
                        "http://localhost:4200/"
                ));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(List.of("Authorization"));
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

    @Bean
   public PasswordEncoder passwordEncoder (){

        return new BCryptPasswordEncoder();
    }

}
