package dev.woori.wooriLog.domain.project.entity;

import dev.woori.wooriLog.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "project_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
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
