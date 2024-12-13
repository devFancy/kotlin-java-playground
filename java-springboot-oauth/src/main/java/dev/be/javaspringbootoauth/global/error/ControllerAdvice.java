package dev.be.javaspringbootoauth.global.error;

import dev.be.javaspringbootoauth.auth.exception.EmptyAuthorizationHeaderException;
import dev.be.javaspringbootoauth.auth.exception.InvalidTokenException;
import dev.be.javaspringbootoauth.auth.exception.NotFoundOAuthTokenException;
import dev.be.javaspringbootoauth.auth.exception.NotFoundTokenException;
import dev.be.javaspringbootoauth.auth.exception.ServerErrorOAuthException;
import dev.be.javaspringbootoauth.global.ApiResultResponse;
import dev.be.javaspringbootoauth.global.error.dto.ErrorReportRequest;
import dev.be.javaspringbootoauth.global.error.dto.ErrorResponse;
import dev.be.javaspringbootoauth.member.exception.InvalidMemberException;
import dev.be.javaspringbootoauth.member.exception.NotFoundMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
public class ControllerAdvice {

    public static final String ERROR = "error";

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "The %s field is %s (provided value: %s)"; // %s 필드는 %s (전달된 값: %s)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleMethodArgumentException(final MethodArgumentNotValidException e) {
        FieldError firstFieldError = e.getFieldErrors().get(0);
        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT,
                firstFieldError.getField(), firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_DTO_FIELD, errorMessage);
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (HttpMessageNotReadableException.class) // 잘못된 요청 본문 형식에 대한 에러 메시지를 클라이언트에 반환
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleInvalidRequestBodyException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST_BODY); // 잘못된 형식의 RequestBody 입니다.
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (MissingServletRequestParameterException.class) // @RequestParam의 누락된 파라미터를 처리
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleInvalidRequestParamException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST_PARAM); // 잘못된 형식의 RequestBody 입니다.
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InvalidTokenException.class,
            InvalidMemberException.class,
    })
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleInvalidException(final CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EmptyAuthorizationHeaderException.class,
    })
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleUnauthorizedException(final CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            NotFoundOAuthTokenException.class,
            NotFoundTokenException.class,
            NotFoundMemberException.class,
    })
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleNotFoundException(final CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleMethodNotAllowedException(final CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ServerErrorOAuthException.class)
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handleOAuthException(final CustomException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            Exception.class,
            IOException.class,
    })
    public ResponseEntity<ApiResultResponse<ErrorResponse>> handlerUnexpectedException(final Exception e, final HttpServletRequest request) {
        ErrorReportRequest errorReportRequest = new ErrorReportRequest(request, e);
        log.error(errorReportRequest.getLogMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        ApiResultResponse<ErrorResponse> apiResultResponse = ApiResultResponse.failure(errorResponse, ERROR);
        return new ResponseEntity<>(apiResultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
