package com.petstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableJpaAuditing
public class PetstoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetstoreApiApplication.class, args);
    }

    @Configuration
    public static class OpenAPIConfiguration {

        @Bean
        public OpenAPI petstoreOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("Petstore API")
                            .description("Spring Boot Petstore API for managing pets and orders")
                            .version("1.0.0"));
        }
    }
}
