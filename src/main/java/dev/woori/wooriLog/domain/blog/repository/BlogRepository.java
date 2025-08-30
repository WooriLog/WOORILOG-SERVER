package dev.woori.wooriLog.domain.blog.repository;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long blogId);

    List<Blog> findByMemberId(Long memberId);

    List<Blog> findAllByProject(Project project);
}
