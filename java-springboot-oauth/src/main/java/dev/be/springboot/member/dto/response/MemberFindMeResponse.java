package dev.be.springboot.member.dto.response;

import dev.be.springboot.member.domain.SocialType;
import dev.be.springboot.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class MemberFindMeResponse {

    private final Long id;

    private final String email;

    private final String nickName;

    private final SocialType socialType;

    public MemberFindMeResponse(final Long id, final String email, final String nickName,
                                final SocialType socialType) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.socialType = socialType;
    }

    public MemberFindMeResponse(final Member member) {
        this(member.getId(), member.getEmail(), member.getNickName(), member.getSocialType());
    }
}
