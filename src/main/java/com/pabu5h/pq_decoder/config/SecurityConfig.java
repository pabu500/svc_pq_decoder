package com.pabu5h.pq_decoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // Add this to enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/",
                                "/health",
                                "/hello",
                                "/process_comtrade_file",
                                "/process_pqd_file",
                                "/process_physical_parser"
                        ).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // This is preferred in Spring Boot 3+
                .allowedMethods("GET", "POST","OPTIONS")
                .allowedHeaders("*");
    }
}
