package com.pabu5h.pq_decoder.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(8000)) // 5 seconds for connection timeout
                .setReadTimeout(Duration.ofMillis(55000)) // 55 seconds for read timeout
                .build();
    }
    @Bean(name = "restTemplateShort")
    public RestTemplate restTemplateShort(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(5000)) // 5 seconds for connection timeout
                .setReadTimeout(Duration.ofMillis(13000)) // 34 seconds for read timeout
                .build();
    }

    @Bean(name = "restTemplateLong")
    public RestTemplate restTemplateLong(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMinutes(20)) // 5 seconds for connection timeout
                .setReadTimeout(Duration.ofMinutes(20)) // 34 seconds for read timeout
                .build();
    }
}