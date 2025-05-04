package dev.be.core.api.common.config;

import dev.be.core.api.common.support.filter.v1.HttpRequestAndResponseLoggingFilter;
import dev.be.core.api.common.support.filter.v2.HttpRequestAndResponseLoggingFilterV2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final HttpRequestAndResponseLoggingFilter loggingFilterV1;
    private final HttpRequestAndResponseLoggingFilterV2 loggingFilterV2;

    public FilterConfig(final HttpRequestAndResponseLoggingFilter loggingFilterV1,
                        final HttpRequestAndResponseLoggingFilterV2 loggingFilterV2) {
        this.loggingFilterV1 = loggingFilterV1;
        this.loggingFilterV2 = loggingFilterV2;
    }

    @Bean
    public FilterRegistrationBean<HttpRequestAndResponseLoggingFilter> loggingFilterV1() {
        FilterRegistrationBean<HttpRequestAndResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFilterV1);
        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<HttpRequestAndResponseLoggingFilterV2> loggingFilterV2() {
        FilterRegistrationBean<HttpRequestAndResponseLoggingFilterV2> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFilterV2);
        registrationBean.addUrlPatterns("/api/v2/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
