package dev.be.springboot.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String AUTHORIZATION = "Authorization";
    private static final String API_VERSION = "v1.0.0";
    private static final String API_TITLE = "[서비스명] Server API";
    private static final String API_DESCRIPTION = "[서비스명] Server API 명세서입니다.";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(getSecurityComponents())
                .security(getSecurityRequirements())
                .info(getApiInfo())
                .addServersItem(new Server().url("https://your-production-url.com"))
                .addServersItem(new Server().url("http://localhost:8080"));
    }

    private Components getSecurityComponents() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(AUTHORIZATION);

        return new Components().addSecuritySchemes("bearerAuth", securityScheme);
    }

    private List<SecurityRequirement> getSecurityRequirements() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        return Collections.singletonList(securityRequirement);
    }

    private Info getApiInfo() {
        return new Info()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION);
    }
}

