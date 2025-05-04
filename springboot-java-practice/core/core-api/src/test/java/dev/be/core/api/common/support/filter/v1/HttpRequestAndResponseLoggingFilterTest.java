package dev.be.core.api.support.filter.v1;

import dev.be.core.api.common.support.filter.v1.HttpRequestAndResponseLoggingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class HttpRequestAndResponseLoggingFilterTest {
    @Mock
    private FilterChain filterChain;

    private HttpRequestAndResponseLoggingFilter filter;

    @BeforeEach
    void setUp() {
        filter = new HttpRequestAndResponseLoggingFilter();
    }

    @Test
    @DisplayName("로깅 대상 URL (/api/v1/*)에서는 filterChain 이 호출된다")
    void shouldInvokeFilterChainForLoggingUri() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/log");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
