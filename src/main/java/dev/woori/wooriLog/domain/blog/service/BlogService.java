package dev.woori.wooriLog.domain.blog.service;


import dev.woori.wooriLog.domain.blog.dto.*;
import dev.woori.wooriLog.domain.blog.dto.request.BlogCreateReq;
import dev.woori.wooriLog.domain.blog.dto.response.BlogDetailInfoRes;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.entity.Progress;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
import dev.woori.wooriLog.domain.blog.repository.ProgressRepository;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import dev.woori.wooriLog.domain.project.repository.ProjectMemberRepository;
import dev.woori.wooriLog.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProgressRepository progressRepository;

    /**
     * 프로젝트 ID, 유저ID, Request의 새 문서 데이터를 받아 DB에 저장합니다.
     * 
     * @param projectId 글을 작성한 프로젝트의 id
     * @param userId 글을 작성한 사용자의 id
     * @param request 새로운 글의 데이터가 담긴 request
     */
    @Transactional
    public Long createBlog(Long projectId, Long userId, BlogCreateReq request) {
        // 프로젝트-멤버 관계 조회
        ProjectMember projectMember = projectMemberRepository.findWithMemberAndProjectByIds(projectId, userId)
                .orElseThrow(IllegalArgumentException::new);

        // Progress Entity 생성
        List<Progress> progresses = request.progresses().stream()
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
        Blog blog = blogRepository.findBlogByIdWithDetails(postId)
                .orElseThrow(IllegalArgumentException::new);
        Project project = blog.getProject();
        Member author = blog.getMember();
        return BlogDetailInfoRes.create(
                BlogDto.create(blog),
                createBlogProjectDTO(project, author),
                MemberInfoDto.create(author)
        );
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
}
