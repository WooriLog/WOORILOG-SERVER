package dev.woori.wooriLog.global.auth.service;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.auth.dto.GoogleLoginReq;
import dev.woori.wooriLog.global.auth.dto.GoogleTokenRes;
import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import dev.woori.wooriLog.global.auth.dto.LoginSuccessRes;
import dev.woori.wooriLog.global.auth.feign.FeignProvider;
import dev.woori.wooriLog.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final FeignProvider feignProvider;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    /**
     * 로그인 처리 로직
     * @param code - Google에서 전달받은 인증 코드
     */
    @Transactional
    public LoginSuccessRes login(GoogleLoginReq request) {
        // Google API & OAuth
        GoogleTokenRes token = feignProvider.getGoogleToken(request.authorizationCode());
        GoogleUserInfoRes userInfo = feignProvider.getUserInfo(Constants.BEARER + token.access_token());

        // Member 조회 or 생성
        Member member = memberRepository.findByProviderAndSocialId(Constants.GOOGLE, userInfo.sub()) // 소셜 ID(uuid)를 통한 유저 조회
                .orElseGet(() -> memberRepository.save(Member.create(userInfo, Constants.GOOGLE))); // 해당 유저가 없다면 생성 (회원가입)

        // JWT Token 발급
        String accessToken = jwtProvider.issueAccessToken(member.getId());
        String refreshToken = jwtProvider.issueRefreshToken(member.getId());

        return LoginSuccessRes.create(member, accessToken, refreshToken);
    }
}
