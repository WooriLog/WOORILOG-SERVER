package dev.woori.wooriLog.global.response.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.woori.wooriLog.global.exception.CustomException;
import dev.woori.wooriLog.global.exception.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GoogleErrorDecoder implements ErrorDecoder {


    private final ObjectMapper objectMapper;
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = null;
        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {}

        int status = response.status();

        // 4XX - 클라이언트 요청/인증 문제
        if (status >= 400 && status < 500) {
            GoogleErrorPayload payload = parse(body);
            String code = Optional.ofNullable(payload.error()).orElse("client_error");

            // 400 - 토큰 교환: 잘못된 code/code_verifier 등
            if (methodKey.contains("GoogleTokenFeign#exchangeToken")) {
                if ("invalid_grant".equals(code) || "invalid_request".equals(code)) {
                    log.warn("GoogleTokenFeign - Invalid code || code_verifier");
                    return new FeignException(ErrorBaseCode.INVALID_GOOGLE_AUTHCODE);
                }
            }

            // 403 - 유저정보 API: 토큰 문제
            if (methodKey.contains("GoogleUserinfoFeign#getUser")) {
                if ("invalid_token".equals(code)) {
                    log.warn("GoogleUserInfoFeign - Invalid Token");
                    return new FeignException(ErrorBaseCode.INVALID_GOOGLE_TOKEN);
                }
            }
            // 400 - 그 외 문제들
            log.warn("GoogleFeign - Other Error");
            return new CustomException(payload.error);
        }

        // 500 : 서버 내부 에러
        if (status >= 500) {
            log.warn("FeignException - Server Error");
            return new FeignException(
                    ErrorBaseCode.INTERNAL_SERVER_ERROR
            );
        }

        // 나머지는 기본 디코더
        return defaultDecoder.decode(methodKey, response);
    }

    private GoogleErrorPayload parse(String body) {
        if (body == null || body.isBlank()) return new GoogleErrorPayload(null, null);
        try {
            return objectMapper.readValue(body, GoogleErrorPayload.class);
        } catch (Exception e) {
            return new GoogleErrorPayload(null, null);
        }
    }

    // Google 표준 오류 응답 매핑용
    private record GoogleErrorPayload(String error, String error_description) {}
}