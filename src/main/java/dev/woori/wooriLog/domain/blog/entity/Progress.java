package dev.woori.wooriLog.domain.blog.entity;

import dev.woori.wooriLog.domain.blog.dto.request.ProgressDto;
import dev.woori.wooriLog.domain.blog.enums.ProgressValue;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Progress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProgressValue progress;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    private Progress(ProgressValue progress, String message) {
        this.progress = progress;
        this.message = message;
    }

    public static Progress create(ProgressDto dto) {
        return new Progress(dto.progress(), dto.message());
    }
}
