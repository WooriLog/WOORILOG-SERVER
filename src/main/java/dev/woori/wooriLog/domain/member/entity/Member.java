package dev.woori.wooriLog.domain.member.entity;

import dev.woori.wooriLog.domain.member.dto.MemberUpdateReq;
import dev.woori.wooriLog.global.auth.dto.GoogleEnrollReq;
import dev.woori.wooriLog.global.auth.dto.GoogleUserInfoRes;
import dev.woori.wooriLog.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import java.util.Objects;

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

    @Column(length = 2048)
    private String profileUrl;

    public static Member create(GoogleUserInfoRes userInfo, GoogleEnrollReq req, String provider) {
        return Member.builder()
                .name(req.name())
                .email(userInfo.email())
                .provider(provider)
                .socialId(userInfo.sub())
                .introduce(req.introduce())
                .profileUrl(userInfo.picture())
                .build();
    }

    public void update(MemberUpdateReq request) {
        this.name = request.name();
        this.introduce = request.introduce();
    }

    public void updateSocialId (String socialId) {
        this.socialId = socialId;
    }

    public void checkProfile(String picture) {
        if (!Objects.equals(this.profileUrl, picture)) {
            this.profileUrl = picture;
        }
    }
}
