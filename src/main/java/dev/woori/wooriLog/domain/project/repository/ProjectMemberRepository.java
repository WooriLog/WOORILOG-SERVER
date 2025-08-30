package dev.woori.wooriLog.domain.project.repository;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);

    @Query("SELECT pm.member FROM ProjectMember pm WHERE pm.project = :project")
    List<Member> findMembersByProject(@Param("project") Project project);

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.member = :member")
    List<Project> findProjectsByMember(@Param("member") Member member);

    @Query("SELECT pm FROM ProjectMember pm JOIN FETCH pm.member m JOIN FETCH pm.project p WHERE p.id = :projectId AND m.id = :memberId")
    Optional<ProjectMember> findWithMemberAndProjectByIds(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query("SELECT pm FROM ProjectMember pm JOIN FETCH pm.member m JOIN FETCH pm.project p WHERE p = :project AND m != :author")
    List<ProjectMember> findWithProjectAndNotAuthorByIds(@Param("project") Project project, @Param("author") Member author);
}
