package dev.woori.woorilog.global.auth.feign;

import dev.woori.woorilog.global.auth.dto.GoogleTokenRes;
import dev.woori.woorilog.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "google-token",
        url = "https://oauth2.googleapis.com",
        configuration = FeignConfig.class
)
public interface GoogleTokenFeign {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenRes exchangeToken(@RequestBody MultiValueMap<String, String> form);
}
