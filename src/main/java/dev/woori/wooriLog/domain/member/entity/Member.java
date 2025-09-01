package dev.woori.wooriLog.domain.member.entity;

import dev.woori.wooriLog.global.auth.dto.GoogleEnrollReq;
import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import dev.woori.wooriLog.global.common.BaseEntity;
import jakarta.persistence.Column;
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
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String provider;

    @Column(nullable = false, unique = true)
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

    public void update(String name, String email, String introduce) {
        this.name = name;
        this.email = email;
        this.introduce = introduce;
    }

    public void updateSocialId (String socialId) {
        this.socialId = socialId;
    }
}
