package dev.be.core.api.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        Components components = new Components()
                .addSecuritySchemes(
                        AUTHORIZATION,
                        new SecurityScheme()
                                .name(AUTHORIZATION)
                                .in(SecurityScheme.In.HEADER)
                                .type(SecurityScheme.Type.APIKEY)
                                .description("Bearer ${ACCESS_TOKEN}")
                );
        return new OpenAPI()
                .components(components)
                .addServersItem(new Server().url("http://localhost:8080"))
                .info(getServerInfo());
    }

    private Info getServerInfo() {
        return new Info()
                .title("[Service] Server API")
                .description("[Service] Server API 명세서입니다.")
                .version("1.0.0");
    }
}
