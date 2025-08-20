package com.example.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API testing
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() // Cho phép tất cả API endpoints không cần authentication
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép static resources
                .requestMatchers("/health", "/actuator/**").permitAll() // Cho phép health check
                .anyRequest().authenticated() // Các request khác cần authentication
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Trang login tùy chỉnh
                .defaultSuccessUrl("/", true) // Redirect sau khi login thành công
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
