package dev.be.javaspringbootoauth.auth.dto.response;

// OAuth 인증 URI(Social Login Link)를 전달하는 Dto
public class OAuthUriResponse {

    private String oAuthUri;

    public OAuthUriResponse(final String oAuthUri) {
        this.oAuthUri = oAuthUri;
    }

    public String getoAuthUri() {
        return oAuthUri;
    }
}
