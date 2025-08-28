package dev.woori.wooriLog.global.auth.controller;

import dev.woori.wooriLog.global.auth.dto.*;
import dev.woori.wooriLog.global.auth.service.GoogleOAuthService;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/google/login")
    public ResponseEntity<BaseResponse<?>> googleLogin(@RequestHeader(name = "Authorization") String authorizationCode) {
        authorizationCode = authorizationCode.startsWith("Bearer ") ?
                authorizationCode.replaceFirst("Bearer ", "") : authorizationCode;
        return ApiResponseUtil.success(SuccessCode.OK, googleOAuthService.login(authorizationCode));
    }

    @PostMapping("/google/enroll")
    public ResponseEntity<BaseResponse<?>> googleEnroll(@RequestHeader(name = "Authorization") String googleToken ,@RequestBody GoogleEnrollReq request) {
        googleToken = googleToken.startsWith("Bearer ") ?
                googleToken.replaceFirst("Bearer ", "") : googleToken;
        return ApiResponseUtil.success(SuccessCode.OK, googleOAuthService.enroll(googleToken, request));
    }
}
