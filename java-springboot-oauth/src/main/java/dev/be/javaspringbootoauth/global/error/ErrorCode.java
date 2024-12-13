package dev.be.javaspringbootoauth.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // OAuth
    EMPTY_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "EMPTY_AUTHORIZATION_HEADER", "The Authorization header is missing."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "The token is invalid."),
    NOT_FOUND_OAUTH_TOKEN(HttpStatus.NOT_FOUND, "NOT_FOUND_OAUTH_TOKEN", "The OAuthToken does not exist"),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "NOT_FOUND_TOKEN", "The token does not exist."),
    SERVER_ERROR_OAUTH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR_OAUTH_TOKEN", "There was an issue during communication with the OAuth server."),

    // Member
    INVALID_MEMBER(HttpStatus.BAD_REQUEST, "INVALID MEMBER", "The member information is incorrect."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOT_FOUND MEMBER", "The member does not exist."),

    // Etc
    INVALID_DTO_FIELD(HttpStatus.BAD_REQUEST, "INVALID_DTO_FIELD", "The DTO field is incorrect."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_BODY", "The RequestBody format is incorrect."),
    INVALID_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_PARAM", "The RequestParam format is incorrect."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected error occurred on the server.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
