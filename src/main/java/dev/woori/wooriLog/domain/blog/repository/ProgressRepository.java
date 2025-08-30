package dev.woori.wooriLog.domain.blog.repository;

import dev.woori.wooriLog.domain.blog.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
