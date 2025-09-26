package dev.woori.wooriLog.domain.member.controller;

import dev.woori.wooriLog.domain.member.dto.MemberUpdateReq;
import dev.woori.wooriLog.domain.member.service.MemberService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 이메일을 이용한 유저 검색
    @GetMapping("/members/search")
    public ResponseEntity<?> getMembersInfoByEmail(@RequestParam String email, @UserId Long memberId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.findMembersByEmail(email, memberId));
    }

    // 유저 조회
    @GetMapping("/members")
    public ResponseEntity<?> getMemberInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.getMemberInfo(userId));
    }

    // 유저 프로필 조회
    @GetMapping("/members/profile")
    public ResponseEntity<?> getProfileInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.getProfileInfoById(userId));
    }

    // 유저 프로필 수정
    @PutMapping("/members/profile")
    public ResponseEntity<?> updateMemberInfo(@UserId Long userId, @Valid @RequestBody MemberUpdateReq request) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.updateMemberInfo(userId, request));
    }
}
