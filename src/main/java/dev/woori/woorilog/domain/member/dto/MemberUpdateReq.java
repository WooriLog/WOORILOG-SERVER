package dev.woori.woorilog.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateReq(
        @NotBlank
        String name,
        String introduce
){
}

