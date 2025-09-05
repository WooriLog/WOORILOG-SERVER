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

    @GetMapping("/members/search")
    public ResponseEntity<?> getMembersInfoByEmail(@RequestParam String email, @UserId Long memberId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.findMembersByEmail(email, memberId));
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMemberInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.getMemberInfo(userId));
    }

    @GetMapping("/members/profile")
    public ResponseEntity<?> getProfileInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.getProfileInfoById(userId));
    }

    @PutMapping("/members/profile")
    public ResponseEntity<?> updateMemberInfo(@UserId Long userId, @Valid @RequestBody MemberUpdateReq request) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.updateMemberInfo(userId, request));
    }
}
