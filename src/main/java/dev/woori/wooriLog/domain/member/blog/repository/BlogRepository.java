package dev.woori.wooriLog.domain.member.blog.repository;

import dev.woori.wooriLog.domain.member.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long blogId);
}
