package dev.be.springboot.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


// Cross-Origin Resource Sharing (CORS)를 설정하기 위한 설정 클래스
// 다른 도메인으로부터의 요청을 허용할지 여부를 설정하는 메커니즘을 제공
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,DELETE,TRACE,OPTIONS,PATCH,PUT";

    private final List<String> allowOriginUrlPatterns;

    private final HandlerMethodArgumentResolver authenticationPrincipalArgumentResolver;

    public WebConfig(@Value("${cors.allow-origin.urls}") final List<String> allowOriginUrlPatterns,
                     final HandlerMethodArgumentResolver authenticationPrincipalArgumentResolver) {
        this.allowOriginUrlPatterns = allowOriginUrlPatterns;
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        String[] patterns = allowOriginUrlPatterns.stream()
                .toArray(String[]::new);

        registry.addMapping("/api/**")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowedOrigins(patterns)
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
    }
}
