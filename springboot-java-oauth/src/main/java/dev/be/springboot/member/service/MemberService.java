package dev.be.springboot.member.service;

import dev.be.springboot.member.dto.response.MemberFindMeResponse;
import dev.be.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberFindMeResponse findById(final Long id) {
        return new MemberFindMeResponse(memberRepository.findMemberById(id));
    }
}