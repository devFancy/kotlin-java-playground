package dev.be.core.api.support.filter.v2;

import org.springframework.http.HttpStatus;

public record HttpLogMessageV2(

        String userId,
        String httpMethod,
        String url,
        HttpStatus httpStatus,
        String headers,
        String requestBody,
        String responseBody
) {
    // ...
}
