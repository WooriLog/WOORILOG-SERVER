package dev.woori.woorilog.global.auth.dto;

public record GoogleTokenRes(
    String access_token,
    String refresh_token,
    String id_token,
    String token_type,
    Integer expires_in,
    String scope
){
    
}
