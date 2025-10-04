package dev.woori.woorilog.domain.project.repository;

import dev.woori.woorilog.domain.project.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long projectId);

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.techStack " +
            "ORDER BY p.createdAt DESC ")
    List<Project> findTopByCreatedAtDesc(Pageable pageable);
}
