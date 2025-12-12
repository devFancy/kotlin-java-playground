package io.dodn.commerce.core.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestClient

@TestConfiguration
class TestRestClientConfig {

    @Bean
    fun mockRestServiceServer(builder: RestClient.Builder): MockRestServiceServer {
        return MockRestServiceServer.bindTo(builder).build()
    }

    @Bean
    @Primary
    fun testRestClient(builder: RestClient.Builder, server: MockRestServiceServer): RestClient {
        return builder
            .baseUrl("https://commerce.free.beeceptor.com/api")
            .build()
    }
}
