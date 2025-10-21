package dev.woori.woorilog.global.auth.service;

import dev.woori.woorilog.domain.member.entity.Member;
import dev.woori.woorilog.domain.member.repository.MemberRepository;
import dev.woori.woorilog.global.auth.Constants;
import dev.woori.woorilog.global.auth.dto.*;
import dev.woori.woorilog.global.auth.feign.FeignProvider;
import dev.woori.woorilog.global.auth.jwt.JwtProvider;
import dev.woori.woorilog.global.auth.jwt.Token;
import dev.woori.woorilog.global.cache.EnrollCache;
import dev.woori.woorilog.global.exception.UnEnrolledException;
import dev.woori.woorilog.global.exception.WooriLogUseException;
import dev.woori.woorilog.global.response.error.ErrorBaseCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static dev.woori.woorilog.global.response.error.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final FeignProvider feignProvider;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final EnrollCache enrollCache;

    /**
     * 로그인 로직
     * @param request 인증 코드가 담긴 요청 DTO
     * @return LoginSuccessRes - 로그인 성공 응답 DTO
     */
    @Transactional
    public LoginSuccessRes login(GoogleLoginReq request) {
        // Google API & OAuth
        GoogleTokenRes googleToken = feignProvider.getGoogleToken(request.authorizationCode());
        GoogleUserInfoRes userInfo = getUserInfo(googleToken.access_token());

        // 회원가입 필요
        if (!memberRepository.existsMemberByProviderAndSocialId(Constants.GOOGLE, userInfo.sub())) {
            String ticket = issueTicket(googleToken.access_token());
            throw new UnEnrolledException(ticket);
        }

        Member member = memberRepository.findByProviderAndSocialId(Constants.GOOGLE, userInfo.sub()) // 소셜 ID를 통한 유저 조회
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

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
    public LoginSuccessRes enroll(String ticket, GoogleEnrollReq request) {
        if (isStartWithBearer(ticket)) {
            ticket = ticket.replace(Constants.BEARER, "");
        }

        String accessToken = enrollCache.findAndConsume(ticket)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_TOKEN));

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
        if (!isStartWithBearer(accessToken))
            accessToken = Constants.BEARER + accessToken;
        return feignProvider.getUserInfo(accessToken);
    }

    private void isEnrolled(String email) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new WooriLogUseException(ErrorBaseCode.CONFLICT);
        }
    }

    private String issueTicket(String accessToken) {
        String ticket = UUID.randomUUID().toString();
        enrollCache.save(ticket, accessToken);
        return ticket;
    }

    private static boolean isStartWithBearer(String header) {
        return StringUtils.hasText(header) && header.startsWith(Constants.BEARER);
    }
}
