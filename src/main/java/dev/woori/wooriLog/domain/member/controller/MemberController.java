package dev.woori.wooriLog.domain.member.controller;

import dev.woori.wooriLog.domain.member.service.MemberService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/search")
    public ResponseEntity<?> getMembersInfoByEmail(@RequestParam String email) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.findMembersByEmail(email));
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMemberInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.findMemberById(userId));
    }

    @GetMapping("/members/profile")
    public ResponseEntity<?> getProfileInfoById(@UserId Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, memberService.findProfileInfoById(userId));
    }

}
