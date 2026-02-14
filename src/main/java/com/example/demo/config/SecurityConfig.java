package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // For REST API, CSRF can be disabled

                // Add security headers
                .headers(headers -> headers
                        .xssProtection(xss -> xss.block(true))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/api/orders/**").authenticated()
                        .antMatchers("/api/cart/**").authenticated()
                        .antMatchers("/api/items/**").permitAll()
                        .anyRequest().permitAll()
                )

                // Basic Auth
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
