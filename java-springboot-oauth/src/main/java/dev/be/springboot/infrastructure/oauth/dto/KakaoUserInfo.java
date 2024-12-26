package dev.be.springboot.infrastructure.oauth.dto;

public class KakaoUserInfo {

    private KakaoAccount kakao_account;

    private KakaoUserInfo() {
    }

    public KakaoUserInfo(final KakaoAccount kakao_account) {
        this.kakao_account = kakao_account;
    }

    public KakaoAccount getKakao_account() {
        return kakao_account;
    }

    public static class KakaoAccount {

        private String email;
        private Profile profile;

        private KakaoAccount() {
        }

        public KakaoAccount(final String email, final Profile profile) {
            this.email = email;
            this.profile = profile;
        }

        public String getEmail() {
            return email;
        }

        public Profile getProfile() {
            return profile;
        }

        public static class Profile {

            private String nickname;

            public Profile() {
            }

            public Profile(final String nickname) {
                this.nickname = nickname;
            }

            public String getNickname() {
                return nickname;
            }
        }
    }
}
