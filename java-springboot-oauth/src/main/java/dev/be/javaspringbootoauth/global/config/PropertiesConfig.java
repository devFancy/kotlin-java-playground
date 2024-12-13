package dev.be.javaspringbootoauth.global.config;

import dev.be.javaspringbootoauth.global.config.oauth.GoogleProperties;
import dev.be.javaspringbootoauth.global.config.oauth.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        GoogleProperties.class,
        KakaoProperties.class
})
public class PropertiesConfig {
}
