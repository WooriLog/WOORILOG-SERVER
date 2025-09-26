package dev.woori.wooriLog.global.auth.service;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.auth.dto.*;
import dev.woori.wooriLog.global.auth.feign.FeignProvider;
import dev.woori.wooriLog.global.auth.jwt.JwtProvider;
import dev.woori.wooriLog.global.auth.jwt.Token;
import dev.woori.wooriLog.global.exception.UnEnrolledException;
import dev.woori.wooriLog.global.exception.WooriLogUseException;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final FeignProvider feignProvider;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    /**
     * 로그인 로직
     * @param request 인증 코드가 담긴 요청 DTO
     * @return LoginSuccessRes - 로그인 성공 응답 DTO
     */
    @Transactional
    public LoginSuccessRes login(GoogleLoginReq request) {
        log.info("[Login] Login Request");

        // Google API & OAuth
        GoogleTokenRes googleToken = feignProvider.getGoogleToken(request.authorizationCode());
        GoogleUserInfoRes userInfo = getUserInfo(googleToken.access_token());

        // Member 조회 or 생성
        Member member = memberRepository.findByProviderAndSocialId(Constants.GOOGLE, userInfo.sub()) // 소셜 ID를 통한 유저 조회
                .orElseThrow(() -> new UnEnrolledException(googleToken.access_token()));

        // 프로필 이미지 변경시 변경사항 적용
        member.checkProfile(userInfo.picture());

        // JWT Token 발급
        Token token = jwtProvider.issueToken(member.getId());

        return LoginSuccessRes.create(member, token);
    }

    /**
     * 회원가입 로직
     * @param request 인증 코드 및 회원가입 정보가 담긴 요청 (GoogleEnrollReq)
     * @return LoginSuccessRes
     */
    @Transactional
    public LoginSuccessRes enroll(final String accessToken, GoogleEnrollReq request) {
        log.info("[Auth Service] Member Enroll : request={}", request);

        GoogleUserInfoRes userInfo = getUserInfo(accessToken);
        isEnrolled(userInfo.email());
        Member member = memberRepository.save(Member.create(userInfo, request, Constants.GOOGLE));

        // JWT Token 발급
        Token token = jwtProvider.issueToken(member.getId());

        return LoginSuccessRes.create(member, token);
    }

    /**
     * @param accessToken 구글에서 전달받은 엑세스 토큰
     * @return GoogleUserInfoRes 구글에서 전달받은 유저정보
     */
    private GoogleUserInfoRes getUserInfo(String accessToken) {
        return feignProvider.getUserInfo(Constants.BEARER + accessToken);
    }

    private void isEnrolled(String email) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new WooriLogUseException(ErrorBaseCode.CONFLICT);
        }
    }
}
