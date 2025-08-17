package dev.woori.wooriLog.domain.member.entity;

import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String provider;

    private String socialId;

    public static Member create(String email, String name, String provider, String socialId) {
        return Member.builder()
                .email(email)
                .name(name)
                .provider(provider)
                .socialId(socialId)
                .build();
    }

    public static Member create(GoogleUserInfoRes userInfo, String provider) {
        return Member.builder()
                .email(userInfo.email())
                .name(userInfo.name())
                .provider(provider)
                .socialId(userInfo.sub())
                .build();
    }

    public void updateSocialId (String socialId) {
        this.socialId = socialId;
    }
}
