package dev.be.springboot.common;

import dev.be.springboot.member.domain.SocialType;
import dev.be.springboot.member.domain.entity.Member;
import dev.be.springboot.member.dto.response.MemberFindMeResponse;

public class MemberFixtures {

    /* 팬시 */
    public static final Long FANCY_ID = 1L;
    public static final String 팬시_이메일 = "fancy@gmail.com";
    public static final String 팬시_닉네임 = "fancy";
    public static final SocialType 소셜_로그인_유형_구글 = SocialType.GOOGLE;

    public static final MemberFindMeResponse 팬시_응답 = new MemberFindMeResponse(FANCY_ID, 팬시_이메일, 팬시_닉네임, 소셜_로그인_유형_구글);

    public static Member 팬시() {
        return Member.builder()
                .email(팬시_이메일)
                .nickName(팬시_닉네임)
                .socialType(소셜_로그인_유형_구글)
                .build();
    }
}
