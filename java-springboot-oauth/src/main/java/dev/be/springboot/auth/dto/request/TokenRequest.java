package dev.be.springboot.auth.dto.request;


import javax.validation.constraints.NotBlank;

public class TokenRequest {

    @NotBlank(message = "인가 코드는 공백일 수 없습니다.")
    private String code;

    @NotBlank(message = "redirectUri는 Null일 수 없습니다.")
    private String redirectUri;

    private TokenRequest() {
    }

    public TokenRequest(final String code, final String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
