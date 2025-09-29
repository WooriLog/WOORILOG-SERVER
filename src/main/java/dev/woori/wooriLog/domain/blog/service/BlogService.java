package dev.woori.wooriLog.domain.blog.service;


import dev.woori.wooriLog.domain.blog.dto.*;
import dev.woori.wooriLog.domain.blog.dto.request.BlogCreateOrUpdateReq;
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
import java.util.Optional;

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
    public Long createBlog(Long projectId, Long userId, BlogCreateOrUpdateReq request) {
        log.info("[Blog Service] Create Blog : projectId={}, userId={}", projectId, userId);
        // 프로젝트-멤버 관계 조회
        ProjectMember projectMember = projectMemberRepository.findWithMemberAndProjectByIds(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException(RELATION_NOT_FOUND));

        // Progress Entity 생성
        List<Progress> progresses = filterAndCreateProgress(request);

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
    public BlogDetailInfoRes getBlogInfo(Optional<Long> memberId, Long postId) {
        log.info("[Blog Service] Get Blog Info : blogId={}", postId);
        Blog blog = blogRepository.findBlogByIdWithDetails(postId)
                .orElseThrow(() -> new EntityNotFoundException(BLOG_NOT_FOUND));

        Project project = blog.getProject();
        Member author = blog.getMember();
        boolean isAuthor = checkAuthor(memberId, author.getId());

        return BlogDetailInfoRes.create(
                isAuthor,
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
     * 블로그 글 작성자 id와 요청을 보낸 사용자 id가 일치하지 않으면 예외 발생
     * @param userId 사용자 id
     * @param blogId 블로그 id
     * @param request 블로그 수정 폼에 담긴 내용들
     * @return blogId 블로그 id
     */
    @Transactional
    public Long updateBlog(Long userId, Long blogId, BlogCreateOrUpdateReq request) {
        log.info("[Blog Service] Update Blog : blogId={}", blogId);
        Blog blog = findBlogAndCheckOwnerShip(userId, blogId);
        List<Progress> progresses = filterAndCreateProgress(request);
        blog.update(request, progresses);
        return blogId;
    }

    /**
     * 블로그 id를 받아와 해당 블로그 글을 삭제
     * 블로그 글 작성자 id와 요청을 보낸 사용자 id가 일치하지 않으면 예외 발생
     * @param userId 사용자 id
     * @param blogId 블로그 id
     */
    @Transactional
    public void deleteBlog(Long userId, Long blogId) {
        log.info("[Blog Service] Delete Blog : blogId={}", blogId);
        Blog blog = findBlogAndCheckOwnerShip(userId, blogId);
        blogRepository.delete(blog);
    }

    /**
     * 블로그 글 id를 통해 블로그 글을 가져오고 글의 작성자인지 확인하는 메서드
     * @param userId 사용자 id
     * @param blogId 블로그 id
     * @return Blog 요청을 보낸 사람이 작성자인 게 확인된 블로그 entity
     */
    private Blog findBlogAndCheckOwnerShip(Long userId, Long blogId) {
        Blog blog = blogRepository.findByIdWithMember(blogId).orElseThrow(() -> new EntityNotFoundException(BLOG_NOT_FOUND));
        if (!blog.getMember().getId().equals(userId)) {
            throw new AccessDeniedException(BLOG_ACCESS_DENIED);
        }
        return blog;
    }

    /**
     * 글의 작성자인지 확인하는 메서드
     * @param memberId 조회한 유저의 memberId
     * @param authorId 작성자의 memberId
     * @return boolean 조회한 클라이언트가 작성자인지 여부
     */
    private static boolean checkAuthor(Optional<Long> memberId, Long authorId) {
        return memberId.filter(id -> id.equals(authorId)).isPresent();
    }

    private static List<Progress> filterAndCreateProgress(BlogCreateOrUpdateReq request) {
        return request.progresses().stream()
                .filter(progressDto -> progressDto.message() != null && !progressDto.message().isBlank())
                .map(Progress::create)
                .toList();
    }
}
