package dev.woori.woorilog.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.woori.woorilog.global.response.error.GoogleErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "dev.woori.woorilog")
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new GoogleErrorDecoder(objectMapper);
    }
}
