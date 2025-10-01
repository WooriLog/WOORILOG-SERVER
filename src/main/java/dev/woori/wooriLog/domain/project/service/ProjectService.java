package dev.woori.wooriLog.domain.project.service;

import dev.woori.wooriLog.domain.blog.dto.BlogInfoDto;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.dto.ProjectMemberDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.domain.project.dto.ProjectBasicInfoDto;
import dev.woori.wooriLog.domain.project.dto.request.ProjectCreateReq;
import dev.woori.wooriLog.domain.project.dto.ProjectDetailInfoDto;
import dev.woori.wooriLog.domain.project.dto.response.ProjectInfoRes;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.entity.ProjectMember;
import dev.woori.wooriLog.domain.project.repository.ProjectMemberRepository;
import dev.woori.wooriLog.domain.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.woori.wooriLog.domain.DomainConstants.LEADER;
import static dev.woori.wooriLog.domain.DomainConstants.MEMBER;
import static dev.woori.wooriLog.global.response.error.ErrorMessage.*;

@Slf4j
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
    public Long createProject(Long leaderId, ProjectCreateReq request) {
        log.info("[Project Service] Create Project");
        // 프로젝트 생성
        Project project = Project.create(request);
        Long projectId = projectRepository.save(project).getId();

        // 리더를 프로젝트에 등록
        addLeaderToProject(leaderId, project);

        // 멤버들을 프로젝트에 등록
        addMembersToProject(leaderId, request, project);

        return projectId;
    }

    /**
     * 프로젝트 정보 조회 메서드
     * @param projectId 프로젝트 Id
     * @return ProjectInfoRes
     */
    @Transactional
    public ProjectInfoRes getProjectInfo(Long projectId) {
        log.info("[Project Service] getProjectInfo : projectId={}", projectId);

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
                ProjectDetailInfoDto.create(project),
                blogInfos,
                memberInfos
        );
    }

    /**
     * 유저가 속한 프로젝트들을 리스트로 반환
     * @param memberId 유저 ID
     * @return List<ProjectDetailInfoDto>
     */
    public List<ProjectDetailInfoDto> getProjectListByMemberId(Long memberId) {
        log.info("[Project Service] getProjectListByMemberId : memberId={}", memberId);

        Member member = findMemberBy(memberId);
        List<Project> projectList = projectMemberRepository.findProjectsByMember(member);

        return projectList.stream()
                .map(ProjectDetailInfoDto::create)
                .toList();
    }

    /**
     * 홈 화면에 전달할 최신 5개 프로젝트 기본 정보 반환 메서드
     * @return List<ProjectBasicInfoDto>
     */
    public List<ProjectBasicInfoDto> getProjectBasicInfos() {
        log.info("[Project Service] getProjectBasicInfos");

        List<Project> topByCreatedAtDesc =
                projectRepository.findTopByCreatedAtDesc(PageRequest.of(0, 5));

        return topByCreatedAtDesc.stream()
                .map(project -> {
                    int blogCount = blogRepository.countBlogByProjectId(project);
                    return ProjectBasicInfoDto.create(project, blogCount);
                })
                .toList();
    }

    /**
     * 프로젝트 삭제 메서드
     * @param projectId 삭제할 프로젝트 ID
     * @param leaderId 요청 유저 ID
     */
    @Transactional
    public void deleteProject(Long projectId, Long leaderId) {
        Project project = findProjectBy(projectId);
        // TODO : Spring Interceptor를 적용하여 추후 분리
        checkAccessPermission(projectId, leaderId);
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject(project);
        projectMemberRepository.deleteAll(projectMembers);
        projectRepository.delete(project);
    }

    /**
     * 프로젝트 멤버 추가
     * @param projectId 프로젝트 ID
     * @param leaderId 요청 유저 ID
     * @param memberId 프로젝트에 추가할 유저 ID
     */
    @Transactional
    public void addProjectMember(Long projectId, Long leaderId, Long memberId) {
        Project project = findProjectBy(projectId);
        Member requestMember = findMemberBy(memberId);
        // TODO : Spring Interceptor를 적용하여 추후 분리
        checkAccessPermission(projectId, leaderId);
        if (projectMemberRepository.existsByMemberIdAndProjectId(memberId, projectId)) {
            throw new IllegalArgumentException(DUPLICATED_REQUEST);
        }
        projectMemberRepository.save(ProjectMember.of(requestMember, project, MEMBER));
    }

    /**
     * 프로젝트 멤버 삭제
     * @param projectId 프로젝트 ID
     * @param leaderId 요청 유저 ID
     * @param memberId 프로젝트에서 제거할 유저 ID
     */
    @Transactional
    public void deleteProjectMember(Long projectId, Long leaderId, Long memberId) {
        checkAccessPermission(projectId, leaderId);
        if (memberId.equals(leaderId)) {
            throw new IllegalArgumentException(LEADER_CAN_NOT_DELETE);
        }
        ProjectMember projectMember = projectMemberRepository.findByProject_IdAndMember_Id(projectId, memberId)
                .orElseThrow(() -> new EntityNotFoundException(RELATION_NOT_FOUND));
        projectMemberRepository.delete(projectMember);
    }

    private void addLeaderToProject(Long leaderId, Project project) {
        Member leader = findMemberBy(leaderId);
        projectMemberRepository.save(ProjectMember.of(leader, project, LEADER));
    }

    private void addMembersToProject(Long leaderId, ProjectCreateReq request, Project project) {
        List<ProjectMemberDto> members = request.members();
        if (members == null || members.isEmpty()) {
            return;
        }

        List<Long> memberIds = members.stream()
                .filter(Objects::nonNull)
                .map(ProjectMemberDto::userId)
                .filter(userId -> !Objects.equals(userId, leaderId))
                .toList();

        // N+1 문제 개선을 위해 전체 멤버 조회 후 Map으로 저장하여 Map에서 필터링 및 DTO 변환 -> O(1)으로 유저 탐색 가능
        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        List<ProjectMember> relations = members.stream()
                .filter(Objects::nonNull)
                .filter(memberInfo -> !Objects.equals(memberInfo.userId(), leaderId))
                .map(memberInfo -> {
                    Member member = memberMap.get(memberInfo.userId());
                    if (member == null || !member.getEmail().equals(memberInfo.email())) {
                        throw new IllegalArgumentException(INVALID_MEMBER_EMAIL + memberInfo.email());
                    }
                    return ProjectMember.of(member, project, memberInfo.role());
                }).toList();

        projectMemberRepository.saveAll(relations);
    }

    private Project findProjectBy(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_FOUND));
    }

    private Member findMemberBy(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private void checkAccessPermission(Long projectId, Long memberId) {
        if (!projectMemberRepository.existsByProjectIdAndMemberIdAndRole(projectId, memberId, LEADER)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}
