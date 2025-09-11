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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final List<String> whiteList;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // CORS preflight 스킵
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final boolean isPermitAll = isPermit(request);
        final String accessToken = getAccessToken(request);
        // 화이트리스트 URI의 경우 통과
        if (isPermitAll) {
            if (accessToken != null) {
                try {
                    final long userId = jwtProvider.getUserIdFromClaims(accessToken);
                    doAuthentication(accessToken, userId);
                } catch (JwtTokenException e) {
                    // 유효하지 않은 토큰의 경우 catch문에 잡혀 Authentication을 생성하지 않음
                    log.warn("[JwtAuthenticationFilter] Invalid JwtToken from Anonymous User", e);
                }
            }
            filterChain.doFilter(request, response);
            return;
        }
        // 화이트리스트가 아닌 경우 token이 존재하지 않으면 UNAUTHORIZED 예외 처리
        if (accessToken == null) {
            throw new JwtTokenException(ErrorBaseCode.UNAUTHORIZED);
        }
        final long userId = jwtProvider.getUserIdFromClaims(accessToken);
        doAuthentication(accessToken, userId);
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

    private boolean isPermit(final HttpServletRequest request) {
        String uri = request.getRequestURI();

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            if (pathMatcher.match("/api/projects/list", uri)) {
                return false;
            }
            if (pathMatcher.match("/api/projects/*", uri) || pathMatcher.match("/api/blog/*", uri)) {
                return true;
            }
        }
        return whiteList.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}
