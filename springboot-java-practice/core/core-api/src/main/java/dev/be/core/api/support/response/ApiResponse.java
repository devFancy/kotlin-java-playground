package dev.be.core.api.support.response;

import dev.be.core.api.support.error.ErrorMessage;
import dev.be.core.api.support.error.ErrorType;

public class ApiResponse<S> {

    private final ResultType resultType;
    private final S data;
    private final ErrorMessage errorMessage;

    public ApiResponse(final ResultType resultType, final S data, final ErrorMessage errorMessage) {
        this.resultType = resultType;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(final S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(final ErrorType errorType) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(errorType));
    }

    public static ApiResponse<?> error(final ErrorType error, final Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }

    public ResultType getResultType() {
        return resultType;
    }

    public S getData() {
        return data;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
