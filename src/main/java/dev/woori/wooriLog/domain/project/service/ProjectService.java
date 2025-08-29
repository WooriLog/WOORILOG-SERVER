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
import dev.woori.wooriLog.global.exception.CustomBaseException;
import dev.woori.wooriLog.global.exception.WooriLogUseException;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.woori.wooriLog.domain.DomainConstants.LEADER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;

    /**
     * 프로젝트 생성 메서드
     * @param leaderId 팀장의 memberId
     * @param request 프로젝트 생성 요청 DTO
     */
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

    /**
     * 프로젝트 정보 조회 메서드
     * @param projectId 프로젝트 Id
     * @return ProjectInfoRes
     */
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

    /**
     * 유저가 속한 프로젝트들을 리스트로 반환
     * @param memberId 유저 ID
     * @return List<ProjectInfoDto>
     */
    public List<ProjectInfoDto> getProjectListByMemberId(Long memberId) {
        Member member = findMemberBy(memberId);
        List<Project> projectList = projectMemberRepository.findProjectsByMember(member);

        return projectList.stream()
                .map(ProjectInfoDto::create)
                .toList();
    }

    private void addLeaderToProject(Long leaderId, Project project) {
        Member leader = findMemberBy(leaderId);
        validateNotJoined(project, leader);
        projectMemberRepository.save(ProjectMember.of(leader, project, LEADER));
    }

    private void addMembersToProject(ProjectCreateReq request, Project project) {
        List<ProjectMemberDto> members = request.members();
        if (members == null || members.isEmpty()) {
            return;
        }

        List<Long> memberIds = members.stream()
                .filter(Objects::nonNull)
                .map(ProjectMemberDto::userId)
                .toList();

        // N+1 문제 개선을 위해 전체 멤버 조회 후 Map으로 저장하여 Map에서 필터링 및 DTO 변환 -> O(1)으로 유저 탐색 가능
        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        List<ProjectMember> relations = members.stream()
                .filter(Objects::nonNull)
                .map(memberInfo -> {
                    Member member = memberMap.get(memberInfo.userId());
                    if (member == null || !member.getEmail().equals(memberInfo.email())) {
                        throw new IllegalArgumentException();
                    }
                    validateNotJoined(project, member);
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

    private void validateNotJoined(Project project, Member member) {
        if (projectMemberRepository.existsByProjectAndMember(project, member)) {
            throw new WooriLogUseException(ErrorBaseCode.CONFLICT);
        }
    }
}
