package dev.woori.wooriLog.domain.member.dto;

public record MemberUpdateReq(
        String name,
        String email,
        String introduce
){
}

