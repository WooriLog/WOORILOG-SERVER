package dev.woori.wooriLog.domain.member.entity;

import dev.woori.wooriLog.global.auth.dto.GoogleEnrollReq;
import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import dev.woori.wooriLog.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String provider;

    private String socialId;

    private String introduce;

    public static Member create(String email, String name, String provider, String socialId) {
        return Member.builder()
                .email(email)
                .name(name)
                .provider(provider)
                .socialId(socialId)
                .build();
    }

    public static Member create(GoogleUserInfoRes userInfo, GoogleEnrollReq req, String provider) {
        return Member.builder()
                .name(req.name())
                .email(userInfo.email())
                .provider(provider)
                .socialId(userInfo.sub())
                .introduce(req.introduce())
                .build();
    }

    public void updateSocialId (String socialId) {
        this.socialId = socialId;
    }
}
