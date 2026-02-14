package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0")
                        .description("Comprehensive API documentation for the E-commerce platform")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Rajat Kumar Sen")
                                .email("rajat@example.com")
                                .url("https://example.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}
