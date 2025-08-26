package dev.woori.wooriLog.domain.blog.service;


import dev.woori.wooriLog.domain.blog.dto.*;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
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

    /**
     * 프로젝트 ID, 유저ID, Request의 새 문서 데이터를 받아 DB에 저장합니다.
     * 
     * @param projectId 글을 작성한 프로젝트의 id
     * @param userId 글을 작성한 사용자의 id
     * @param request 새로운 글의 데이터가 담긴 request
     */
    @Transactional
    public void createBlog(Long projectId, Long userId, BlogCreateReq request) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalArgumentException::new);
        Member member = memberRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        Blog newBlog = Blog.create(project, member, request);
        blogRepository.save(newBlog);
    }

    /**
     * 글을 열람하기 위한 글과 작성자, 작성 프로젝트 응답을 생성합니다.
     * 하위 DTO를 생성하고 조립한 응답을 생성해 리턴합니다.
     * 
     * @param postId 열람할 글 id
     * @return BlogInfoRes: 열람할 글, 작성자, 작성 프로젝트의 정보
     */
    public BlogInfoRes createBlogInfoRes(Long postId) {
        Blog blog = blogRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        Project project = blog.getProject();
        Member member = blog.getMember();
        return BlogInfoRes.create(
                BlogPostDTO.create(
                        blog.getTitle(),
                        blog.getTags(),
                        blog.getCategory(),
                        blog.getDocument(),
                        blog.getCreatedAt(),
                        blog.getUpdatedAt()),
                createBlogProjectDTO(project),
                BlogMemberDTO.create(member)
        );
    }

    /**
     * 열람할 글을 작성한 프로젝트에 대한 DTO를 생성해 리턴합니다.
     *
     * @param project 열람할 글을 작성한 프로젝트의 엔티티
     * @return BlogProjectDTO: 열람할 글을 작성한 프로젝트의 DTO
     */
    private BlogProjectDTO createBlogProjectDTO(Project project) {
        List<ProjectMember> projectMember = projectMemberRepository.findByProject(project);

        List<BlogMemberDTO> memberList = projectMember.stream().map(pm -> BlogMemberDTO.create(pm.getMember())).toList();

        return BlogProjectDTO.create(
                project.getProjectName(),
                project.getIntroduce(),
                memberList
        );
    }
}
