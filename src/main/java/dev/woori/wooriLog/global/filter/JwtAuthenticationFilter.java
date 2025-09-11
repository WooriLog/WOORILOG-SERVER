package dev.woori.wooriLog.global.filter;

import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.auth.jwt.JwtProvider;
import dev.woori.wooriLog.global.auth.jwt.TokenAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static dev.woori.wooriLog.global.auth.jwt.TokenAuthentication.createTokenAuthentication;

/**
 * SpringSecurity FilterChain에 추가할 JWT 관련 Filter
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // CORS preflight 스킵
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessToken(request);

        if (accessToken != null) {
            final long userId = jwtProvider.getUserIdFromClaims(accessToken);
            // Token을 이용한 인증 객체 생성
            doAuthentication(accessToken, userId);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER)) {
            // Bearer를 제외한 순수 aT
            return accessToken.substring(Constants.BEARER.length());
        }
        return null; // 토큰이 없는 익명 사용자
    }

    private void doAuthentication(final String token, final long userId) {
        TokenAuthentication tokenAuthentication = createTokenAuthentication(token, userId);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(tokenAuthentication);
    }
}
