package dev.woori.wooriLog.domain.blog.entity;

import dev.woori.wooriLog.domain.blog.dto.request.BlogCreateReq;
import dev.woori.wooriLog.domain.blog.enums.Category;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long id;

    @Lob
    private String document;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private Long idx;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "blog_id"))
    private List<String> tags;

    @OneToMany(
            mappedBy = "blog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Progress> progresses = new ArrayList<>();

    public static Blog create(Project project, Member member, BlogCreateReq request, List<Progress> progresses) {
        Blog blog = Blog.builder()
                .document(request.document())
                .title(request.title())
                .project(project)
                .member(member)
                .category(request.category())
                .tags(request.tags())
                .build();
        blog.addProgresses(progresses);
        return blog;
    }

    private void addProgress(Progress p) {
        this.progresses.add(p);
        p.setBlog(this);
    }

    public void addProgresses(List<Progress> progresses) {
        if (progresses == null) return;
        for (Progress p : progresses) addProgress(p);
    }

    // id가 커밋 후에 저장되기 때문에 일단 커밋한 후 idx를 업데이트
    // id * 100으로 생성됩니다.
    @PostPersist
    private void afterInsert() {
        this.idx = this.id * 100L;
    }
}
