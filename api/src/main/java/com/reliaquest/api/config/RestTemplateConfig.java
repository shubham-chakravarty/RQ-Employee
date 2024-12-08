package com.reliaquest.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    /**
     * Configures Rest Template Centrally
     * @return RestTemplate Bean
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
