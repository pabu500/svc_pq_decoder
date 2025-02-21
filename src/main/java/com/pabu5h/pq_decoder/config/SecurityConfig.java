package com.pabu5h.pq_decoder.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import static org.springframework.security.config.Customizer.withDefaults;

// Set it as a configuration class
//@Configuration
//@EnableWebMvc
public class SecurityConfig/* implements WebMvcConfigurer */{ // Implement WebMvcConfigurer

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(
//                                "/",
//                                "/health",
//                                "/hello",
//
//                                "/process_comtrade_file",
//                                "/process_pqd_file",
//
//                                "/process_physical_parser"
//                        ).permitAll()
//                        .anyRequest().authenticated());
//        return http.build();
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all endpoints
//                .allowedOrigins("*") // Allow all origins
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name())
//                .allowedHeaders("*");
//
//    }
}
