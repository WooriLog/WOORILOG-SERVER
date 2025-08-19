package dev.woori.wooriLog.global.auth.jwt;

import dev.woori.wooriLog.global.exception.JwtTokenException;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtGenerator jwtGenerator;

    /**
     * accessToken & refreshToken 발급
     * @param userId - memberId
     * @return Token - aT, rT
     */
    public Token issueToken(final long userId) {
        String accessToken = jwtGenerator.generateAccessToken(userId);
        String refreshToken = jwtGenerator.generateRefreshToken(userId);
        return Token.of(accessToken, refreshToken);
    }

    /**
     * Token에서 userId 추출
     * @param token - userId를 추출하기 위한 Token (aT, rT)
     * @return long userId
     */
    public long getUserIdFromClaims(final String token) {
        Claims claims = jwtGenerator.parseToken(token).getBody();
        Object userId = claims.get("userId");

        if (userId == null) {
            throw new JwtTokenException(ErrorBaseCode.UNAUTHORIZED);
        }
        try {
            return Long.parseLong(userId.toString());
        } catch (NumberFormatException e) {
            throw new JwtTokenException(ErrorBaseCode.UNAUTHORIZED);
        }
    }
}
