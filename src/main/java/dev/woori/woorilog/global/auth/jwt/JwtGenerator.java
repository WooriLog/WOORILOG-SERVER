package dev.woori.woorilog.global.auth.jwt;

import dev.woori.woorilog.global.exception.JwtTokenExpiredException;
import dev.woori.woorilog.global.exception.JwtTokenInvalidException;
import dev.woori.woorilog.global.response.error.ErrorBaseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtGenerator {

    private final JwtProperties jwtProperties;

    /**
     * AccessToken 발급
     * @param userId - 커스텀 Claim
     * @return String accessToken
     */
    public String generateAccessToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now, true);

        final Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.issuer())
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * RefreshToken 발급
     * @param userId - 커스텀 Claim
     * @return String refreshToken
     */
    public String generateRefreshToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now, false);

        final Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.issuer())
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date generateExpirationDate(final Date now, final boolean isAccessToken) {
        if (isAccessToken) {
            return new Date(now.getTime() + jwtProperties.accessTokenExpirationTime());
        } else {
            return new Date(now.getTime() + jwtProperties.refreshTokenExpirationTime());
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public Jws<Claims> parseToken(final String token) {
        try {
            final JwtParser jwtParser = getJwtParser();
            return jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            //만료된 jwt 예외처리
            throw new JwtTokenExpiredException(ErrorBaseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            //잘못된 jwt 예외처리
            throw new JwtTokenInvalidException(ErrorBaseCode.UNAUTHORIZED);
        }
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }
}
