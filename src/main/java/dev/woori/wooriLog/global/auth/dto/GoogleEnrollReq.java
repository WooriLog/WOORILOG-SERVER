package dev.woori.wooriLog.global.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleEnrollReq(
        @NotBlank
        String name,

        String introduce
) {
}
