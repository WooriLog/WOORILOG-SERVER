package dev.woori.woorilog.global.auth.feign;

import dev.woori.woorilog.global.auth.Constants;
import dev.woori.woorilog.global.auth.dto.GoogleUserInfoRes;
import dev.woori.woorilog.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Google 사용자 조회 - Feign
 */
@FeignClient(
        name = "google-userinfo",
        url = "https://openidconnect.googleapis.com",
        configuration = FeignConfig.class
)
public interface GoogleUserInfoFeign {

    @GetMapping("/v1/userinfo")
    GoogleUserInfoRes getUserInfo(@RequestHeader(Constants.AUTHORIZATION) String bearerToken);
}
