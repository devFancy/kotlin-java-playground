package dev.be.core.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val AUTHORIZATION = "Authorization"

        val components = Components().addSecuritySchemes(
            AUTHORIZATION,
            SecurityScheme()
                .name(AUTHORIZATION)
                .`in`(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY)
                .description("Bearer \${ACCESS_TOKEN}")
        )
        return OpenAPI()
            .components(components)
            .addServersItem(Server().url("http//localhost:8080"))
            .info(getServerInfo())
    }

    private fun getServerInfo(): Info {
        return Info().title("[Service] Server API")
            .description("[Service] Server API 명세서입니다.")
            .version("1.0.0")

    }

}