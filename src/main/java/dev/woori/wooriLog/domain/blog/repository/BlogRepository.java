package dev.woori.wooriLog.domain.blog.repository;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.enums.Category;
import dev.woori.wooriLog.domain.project.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long blogId);

    @Query("SELECT b FROM Blog b JOIN FETCH b.member WHERE b.id = :blogId")
    Optional<Blog> findByIdWithMember(@Param("blogId") Long blogId);

    @Query("SELECT b FROM Blog b LEFT JOIN FETCH b.tags WHERE b.member.id = :memberId")
    List<Blog> findByMemberId(Long memberId);

    List<Blog> findAllByProject(Project project);

    @Query("SELECT b FROM Blog b JOIN FETCH b.member JOIN FETCH b.project LEFT JOIN FETCH b.progresses WHERE b.id = :id")
    Optional<Blog> findBlogByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Blog b " +
            "LEFT JOIN FETCH b.progresses " +
            "JOIN FETCH b.member " +
            "JOIN FETCH b.project " +
            "WHERE b.category != 'CHECKPOINT' " +
            "ORDER BY b.createdAt DESC "
    )
    List<Blog> findTopOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT COUNT(b) FROM Blog b WHERE b.project = :project AND b.category != 'CHECKPOINT'")
    int countBlogByProjectId(@Param("project") Project project);
}
