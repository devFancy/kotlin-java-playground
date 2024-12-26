package dev.be.springboot.member.controller;

import dev.be.springboot.auth.service.authentication.AuthenticationPrincipal;
import dev.be.springboot.global.ApiResultResponse;
import dev.be.springboot.member.dto.response.MemberFindMeResponse;
import dev.be.springboot.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "유저 관련 API")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "로그인한 유저 정보를 조회한다.")
    @GetMapping("/api/v1/members/me")
    public ResponseEntity<ApiResultResponse<MemberFindMeResponse>> getMyInfo(@AuthenticationPrincipal final Long loginMemberId) {
        MemberFindMeResponse response = memberService.findById(loginMemberId);
        ApiResultResponse<MemberFindMeResponse> apiResultResponse = ApiResultResponse.success(response, "member");
        return new ResponseEntity<>(apiResultResponse, HttpStatus.OK);
    }
}
