package dev.woori.woorilog.global.auth.controller;

import dev.woori.woorilog.global.auth.dto.*;
import dev.woori.woorilog.global.auth.service.GoogleOAuthService;
import dev.woori.woorilog.global.response.ApiResponseUtil;
import dev.woori.woorilog.global.response.BaseResponse;
import dev.woori.woorilog.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/google/login")
    public ResponseEntity<BaseResponse<LoginSuccessRes>> googleLogin(@RequestBody GoogleLoginReq request) {
        return ApiResponseUtil.success(SuccessCode.OK, googleOAuthService.login(request));
    }

    @PostMapping("/google/enroll")
    public ResponseEntity<BaseResponse<LoginSuccessRes>> googleEnroll(
            @RequestHeader(name = "Authorization") String ticket,
            @Valid  @RequestBody GoogleEnrollReq request
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, googleOAuthService.enroll(ticket, request));
    }
}
