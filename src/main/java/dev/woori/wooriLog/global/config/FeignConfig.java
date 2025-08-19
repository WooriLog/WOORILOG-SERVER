package dev.woori.wooriLog.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "dev.woori.wooriLog")
public class FeignConfig {
}
