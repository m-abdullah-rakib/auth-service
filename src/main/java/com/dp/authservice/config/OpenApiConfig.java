package com.dp.authservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authentication Service API")
                        .description("API documentation for Authentication Service")
                        .version("v1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                        .schemas(
                                    Map.of("UserResponseSchema",
                                        new Schema<>()
                                                .type("object")
                                                .addProperty("id", new Schema<>().type("integer").format("int64").description("User Id"))
                                                .addProperty("email", new Schema<>().type("string").description("User Email"))
                                                .addProperty("createdAt", new Schema<>().type("string").format("date-time").description("Creation date"))
                                    )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
