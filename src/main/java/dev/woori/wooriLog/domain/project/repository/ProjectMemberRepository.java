package dev.woori.wooriLog.domain.project.repository;

import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
}
