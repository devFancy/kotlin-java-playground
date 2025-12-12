package io.dodn.commerce.core.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class RestClientConfig {
    @Bean
    fun restClient(): RestClient {
        val requestFactory = JdkClientHttpRequestFactory(
            HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build(),
        )
        requestFactory.setReadTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .requestFactory(requestFactory)
            .baseUrl("https://commerce.free.beeceptor.com/api")
            .build()
    }
}
