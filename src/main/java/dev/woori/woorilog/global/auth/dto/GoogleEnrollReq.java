package dev.woori.woorilog.global.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleEnrollReq(
        @NotBlank
        String name,

        String introduce
) {
}
