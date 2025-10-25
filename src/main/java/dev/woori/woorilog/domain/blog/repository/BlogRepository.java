package dev.woori.woorilog.domain.blog.repository;

import dev.woori.woorilog.domain.blog.dto.ProjectBlogCount;
import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT b FROM Blog b LEFT JOIN FETCH b.tags WHERE b.id = :id")
    void findBlogByIdWithTags(@Param("id") Long id);

    @Query("SELECT b.id FROM Blog b " +
            "WHERE b.category != 'CHECKPOINT' "
    )
    Page<Long> findBlogIds(Pageable pageable);

    @Query("SELECT DISTINCT b " +
            "FROM Blog b " +
            "LEFT JOIN FETCH b.progresses " +
            "JOIN FETCH b.member " +
            "JOIN FETCH b.project " +
            "WHERE b.id IN :ids " +
            "ORDER BY b.createdAt DESC")
    List<Blog> findBlogsWithDetailsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT b.project.id as projectId, COUNT(b.id) as blogCount " +
            "FROM Blog b " +
            "WHERE b.project IN :projects AND b.category != dev.woori.woorilog.domain.blog.enums.Category.CHECKPOINT " +
            "GROUP BY b.project.id"
    )
    List<ProjectBlogCount> findBlogCountsByProjects(@Param("projects") List<Project> projects);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Blog b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(@Param("id") Long id);
}
