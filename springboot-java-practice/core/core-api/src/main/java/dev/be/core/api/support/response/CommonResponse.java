package dev.be.core.api.support.response;

import dev.be.core.api.support.error.ErrorMessage;
import dev.be.core.api.support.error.ErrorType;

public class CommonResponse<T> {

    private final ResultType resultType;
    private final T data;
    private final ErrorMessage errorMessage;

    public CommonResponse(final ResultType resultType, final T data, final ErrorMessage errorMessage) {
        this.resultType = resultType;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static CommonResponse<?> success() {
        return new CommonResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> CommonResponse<T> success(final T data) {
        return new CommonResponse<>(ResultType.SUCCESS, data, null);
    }

    public static CommonResponse<?> error(final ErrorType errorType) {
        return new CommonResponse<>(ResultType.ERROR, null, new ErrorMessage(errorType));
    }

    public static CommonResponse<?> error(final ErrorType error, final Object errorData) {
        return new CommonResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }

    public ResultType getResultType() {
        return resultType;
    }

    public T getData() {
        return data;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
