package dev.woori.wooriLog.domain.project.repository;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);

    @Query("SELECT pm.member FROM ProjectMember pm WHERE pm.project = :project")
    List<Member> findMembersByProject(@Param("project") Project project);

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.member = :member")
    List<Project> findProjectsByMember(@Param("member") Member member);
}
