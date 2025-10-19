package com.example.mini_bank_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI miniBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini Bank API")
                        .description("API REST completa para sistema bancário simplificado")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("João Developer")
                                .email("joao@example.com")
                                .url("https://github.com/joaodev"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
