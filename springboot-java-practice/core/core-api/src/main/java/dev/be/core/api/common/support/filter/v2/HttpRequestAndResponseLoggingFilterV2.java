package dev.be.core.api.common.support.filter.v2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpRequestAndResponseLoggingFilterV2 extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(HttpRequestAndResponseLoggingFilterV2.class);
    private static final String TRACE_ID = "traceId";
    private static final String USER_ID = "userId";
    private static final String UNKNOWN = "unknown";

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) {

        final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        final String traceId = UUID.randomUUID().toString().substring(0, 32);
        final String userId = extractUserId(requestWrapper);

        MDC.put(TRACE_ID, traceId);
        MDC.put(USER_ID, userId);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);

            // HttpLogMessage 생성
            HttpLogMessageV2 logMessage = new HttpLogMessageV2(
                    userId,
                    requestWrapper.getMethod(),
                    requestWrapper.getRequestURI(),
                    HttpStatus.valueOf(responseWrapper.getStatus()),
                    getRequestHeaders(requestWrapper),
                    getRequestBody(requestWrapper),
                    getResponseBody(responseWrapper)
            );

            log.info("\n{}", toPrettierLog(logMessage));

        } catch (Exception e) {
            handleException(e);
        } finally {
            try {
                responseWrapper.copyBodyToResponse();
            } catch (IOException copyException) {
                log.error("[HttpRequestAndResponseLoggingFilterV2] I/O exception occurred while copying response body", copyException);
            }
            MDC.remove(TRACE_ID);
            MDC.remove(USER_ID);
        }
    }

    private void handleException(final Exception e) {
        if (e instanceof IOException ioEx) {
            log.error("[HttpRequestAndResponseLoggingFilterV2] I/O exception occurred", ioEx);
            throw new RuntimeException("I/O error occurred while processing request/response", ioEx);
        } else if (e instanceof ServletException servletEx) {
            log.error("[HttpRequestAndResponseLoggingFilterV2] Servlet exception occurred", servletEx);
            throw new RuntimeException("Servlet error occurred while processing request/response", servletEx);
        } else {
            log.error("[HttpRequestAndResponseLoggingFilterV2] Unknown exception occurred", e);
            throw new RuntimeException("Unknown error occurred while processing request/response", e);
        }
    }

    private String toPrettierLog(final HttpLogMessageV2 msg) {
        return """
                [REQUEST] %s %s [RESPONSE - STATUS: %s]
                >> USER_ID: %s
                >> HEADERS: %s
                >> REQUEST_BODY: %s
                >> RESPONSE_BODY: %s
                """.formatted(
                msg.httpMethod(), msg.url(), msg.httpStatus(),
                msg.userId(),
                msg.headers(), msg.requestBody(), msg.responseBody()
        );
    }

    private String extractUserId(final HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (name.equalsIgnoreCase(USER_ID)) {
                String headerUserId = request.getHeader(name);
                if (headerUserId != null && !headerUserId.isEmpty()) {
                    return headerUserId;
                }
            }
        }
        return UNKNOWN;
    }

    private String getRequestHeaders(final HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .map(name -> name + ": " + request.getHeader(name))
                .collect(Collectors.joining("; "));
    }

    private String getRequestBody(final ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        return (buf.length > 0) ? new String(buf, StandardCharsets.UTF_8) : "";
    }

    private String getResponseBody(final ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return (buf.length > 0) ? new String(buf, StandardCharsets.UTF_8) : "";
    }
}
