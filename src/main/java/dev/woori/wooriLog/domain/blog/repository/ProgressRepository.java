package dev.woori.wooriLog.domain.blog.repository;

import dev.woori.wooriLog.domain.blog.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
