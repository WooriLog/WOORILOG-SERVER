package dev.woori.wooriLog.domain.project.service;

import dev.woori.wooriLog.domain.blog.dto.BlogInfoDto;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.dto.ProjectMemberDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.domain.project.dto.ProjectCreateReq;
import dev.woori.wooriLog.domain.project.dto.ProjectInfoDto;
import dev.woori.wooriLog.domain.project.dto.ProjectInfoRes;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import dev.woori.wooriLog.domain.project.repository.ProjectMemberRepository;
import dev.woori.wooriLog.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static dev.woori.wooriLog.domain.DomainConstants.LEADER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public void createProject(Long leaderId, ProjectCreateReq request) {
        // 프로젝트 생성
        Project project = Project.create(request);
        projectRepository.save(project);

        // 리더를 프로젝트에 등록
        addLeaderToProject(leaderId, project);

        // 멤버들을 프로젝트에 등록
        addMembersToProject(request, project);
    }

    @Transactional
    public ProjectInfoRes getProjectInfo(Long projectId) {
        Project project = findProjectBy(projectId);
        List<Member> projectMembers = projectMemberRepository.findMembersByProject(project);
        List<Blog> blogPosts = blogRepository.findAllByProject(project);

        List<MemberInfoDto> memberInfos = projectMembers.stream()
                .map(MemberInfoDto::create)
                .toList();

        List<BlogInfoDto> blogInfos = blogPosts.stream()
                .map(BlogInfoDto::create)
                .toList();

        return ProjectInfoRes.of(
                ProjectInfoDto.create(project),
                blogInfos,
                memberInfos
        );
    }

    private void addLeaderToProject(Long leaderId, Project project) {
        Member leader = findMemberBy(leaderId);
        projectMemberRepository.save(ProjectMember.of(leader, project, LEADER));
    }

    private void addMembersToProject(ProjectCreateReq request, Project project) {
        List<ProjectMemberDto> members = request.members();

        List<ProjectMember> relations = members.stream()
                        .filter(Objects::nonNull)
                                .map(memberInfo -> {
                                    Member member = findMemberBy(memberInfo.userId(), memberInfo.email());
                                    return ProjectMember.of(member, project, memberInfo.role());
                                }).toList();

        projectMemberRepository.saveAll(relations);
    }

    private Project findProjectBy(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(IllegalArgumentException::new);
    }

    private Member findMemberBy(Long userId) {
        return memberRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    }

    private Member findMemberBy(Long userId, String email) {
        return memberRepository.findByIdAndEmail(userId, email).orElseThrow(IllegalArgumentException::new);
    }
}
