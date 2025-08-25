package dev.woori.wooriLog.domain.project.entity;

import dev.woori.wooriLog.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "PROJECT_MEMBER")
@RequiredArgsConstructor
public class ProjectMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_MEMBER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String role;

    private ProjectMember(Member member, Project project, String role) {
        this.member = member;
        this.project = project;
        this.role = role;
    }

    public static ProjectMember of(Member member, Project project, String role) {
        return new ProjectMember(member, project, role);
    }
}
