package dev.woori.wooriLog.global.filter;

import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.auth.jwt.JwtProvider;
import dev.woori.wooriLog.global.auth.jwt.TokenAuthentication;
import dev.woori.wooriLog.global.exception.JwtTokenException;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static dev.woori.wooriLog.global.auth.jwt.TokenAuthentication.createTokenAuthentication;

/**
 * SpringSecurity FilterChain에 추가할 JWT 관련 Filter
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final List<String> whiteList;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        return whiteList.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessToken(request);

        final long userId = jwtProvider.getUserIdFromClaims(accessToken);

        // Token을 이용한 인증 객체 생성
        doAuthentication(accessToken, userId);
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER)) {
            // Bearer를 제외한 순수 aT
            return accessToken.substring(Constants.BEARER.length());
        }
        throw new JwtTokenException(ErrorBaseCode.UNAUTHORIZED);
    }

    private void doAuthentication(final String token, final long userId) {
        TokenAuthentication tokenAuthentication = createTokenAuthentication(token, userId);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(tokenAuthentication);
    }
}
