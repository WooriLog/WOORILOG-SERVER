package dev.woori.wooriLog.global.auth.feign;

import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.auth.dto.GoogleTokenRes;
import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class FeignProvider {
    private final GoogleTokenFeign googleTokenFeign;
    private final GoogleUserInfoFeign googleUserInfoFeign;

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.scope}")
    private String scope;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    /**
     * OAuth를 이용한 Google AccessToken 획득 메서드
     * @param authorizationCode - Google에서 받은 승인 코드
     * @return GoogleTokenRes - 승인코드를 통해 전달 받은 Token DTO
     */
    public GoogleTokenRes getGoogleToken(final String authorizationCode) {
        String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);
        MultiValueMap<String, String> form = createForm(decodedCode);
        return googleTokenFeign.exchangeToken(form);
    }

    /**
     * OAuth를 이용한 Google UserInfo 획득 메서드
     * @param bearToken - getGoogleToken에서 전달 받은 Token
     * @return GoogleUserInfoRes - 유저에 관한 정보 DTO
     */
    public GoogleUserInfoRes getUserInfo(String bearToken) {
        return googleUserInfoFeign.getUserInfo(bearToken);
    }

    private MultiValueMap<String, String> createForm(final String authorizationCode) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", Constants.AUTHORIZATION_CODE);
        form.add("code", authorizationCode);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);

        return form;
    }
}
