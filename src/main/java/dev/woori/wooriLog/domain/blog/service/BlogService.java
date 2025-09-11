package dev.woori.wooriLog.domain.blog.service;


import dev.woori.wooriLog.domain.blog.dto.*;
import dev.woori.wooriLog.domain.blog.dto.request.BlogCreateReq;
import dev.woori.wooriLog.domain.blog.dto.request.BlogUpdateReq;
import dev.woori.wooriLog.domain.blog.dto.response.BlogBasicInfoRes;
import dev.woori.wooriLog.domain.blog.dto.response.BlogDetailInfoRes;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.entity.Progress;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import dev.woori.wooriLog.domain.project.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.woori.wooriLog.global.response.error.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final ProjectMemberRepository projectMemberRepository;

    /**
     * 프로젝트 ID, 유저ID, Request의 새 문서 데이터를 받아 DB에 저장합니다.
     * 
     * @param projectId 글을 작성한 프로젝트의 id
     * @param userId 글을 작성한 사용자의 id
     * @param request 새로운 글의 데이터가 담긴 request
     */
    @Transactional
    public Long createBlog(Long projectId, Long userId, BlogCreateReq request) {
        log.info("[Blog Service] Create Blog : projectId={}, userId={}", projectId, userId);
        // 프로젝트-멤버 관계 조회
        ProjectMember projectMember = projectMemberRepository.findWithMemberAndProjectByIds(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException(RELATION_NOT_FOUND));

        // Progress Entity 생성
        List<Progress> progresses = request.progresses().stream()
                .filter(progressDto -> progressDto.message() != null && !progressDto.message().isBlank())
                .map(Progress::create)
                .toList();

        // Blog Entity 생성
        Blog createdBlog = Blog.create(
                projectMember.getProject(),
                projectMember.getMember(),
                request, progresses
        );

        blogRepository.save(createdBlog);
        return createdBlog.getId();
    }

    /**
     * 글을 열람하기 위한 글과 작성자, 작성 프로젝트 응답을 생성합니다.
     * 하위 DTO를 생성하고 조립한 응답을 생성해 리턴합니다.
     * 
     * @param postId 열람할 글 id
     * @return BlogDetailInfoRes: 열람할 글, 작성자, 작성 프로젝트의 정보
     */
    @Transactional
    public BlogDetailInfoRes getBlogInfo(Long postId) {
        log.info("[Blog Service] Get Blog Info : blogId={}", postId);

        Blog blog = blogRepository.findBlogByIdWithDetails(postId)
                .orElseThrow(() -> new EntityNotFoundException(BLOG_NOT_FOUND));

        Project project = blog.getProject();
        Member author = blog.getMember();

        return BlogDetailInfoRes.create(
                BlogDto.create(blog),
                createBlogProjectDTO(project, author),
                MemberInfoDto.create(author)
        );
    }

    /**
     * 홈 화면에 전달할 최신 5개 블로그 기본 정보 반환 메서드
     * @return List<BlogBasicInfoRes>
     */
    public List<BlogBasicInfoRes> getBlogBasicInfos() {
        log.info("[Blog Service] getBlogBasicInfos");
        List<Blog> top5OrderByCreatedAtDesc =
                blogRepository.findTopOrderByCreatedAtDesc(PageRequest.of(0, 5));

        return top5OrderByCreatedAtDesc.stream()
                .map(BlogBasicInfoRes::create)
                .toList();
    }

    /**
     * 열람할 글을 작성한 프로젝트에 대한 DTO를 생성해 리턴합니다.
     *
     * @param project 열람할 글을 작성한 프로젝트의 엔티티
     * @return BlogProjectDto: 열람할 글을 작성한 프로젝트의 DTO
     */
    private BlogProjectDto createBlogProjectDTO(Project project, Member author) {
        List<ProjectMember> projectMember = projectMemberRepository.findWithProjectAndNotAuthorByIds(project, author);

        List<MemberInfoDto> memberList = projectMember.stream()
                .map(pm -> MemberInfoDto.create(pm.getMember()))
                .toList();

        return BlogProjectDto.create(project, memberList);
    }

    /**
     * 블로그 id와 수정된 블로그 포스팅 정보를 통해 블로그 글을 수정
     * @param blogId 블로그 id
     * @param request 블로그 수정 폼에 담긴 내용들
     * @return blogId 블로그 id
     */
    @Transactional
    public Long updateBlog(Long userId, Long blogId, BlogUpdateReq request) {
        Blog blog = findBlogById(blogId);
        if(!blog.getMember().getId().equals(userId)){
            throw new AccessDeniedException(BLOG_ACCESS_DENIED);
        }
        blog.update(request);
        log.info("[Blog Service] Update Blog : blogId={}", blogId);
        return blogId;
    }

    @Transactional
    public boolean deleteBlog(Long userId, Long blogId) {
        Blog blog = findBlogById(blogId);
        if(!blog.getMember().getId().equals(userId)){
            throw new AccessDeniedException(BLOG_ACCESS_DENIED);
        }
        blogRepository.deleteById(blogId);
        log.info("[Blog Service] Delete Blog : blogId={}", blogId);
        return true;
    }

    private Blog findBlogById(Long blogId) {
        return blogRepository.findById(blogId).orElseThrow(() -> new EntityNotFoundException(BLOG_NOT_FOUND));
    }
}
