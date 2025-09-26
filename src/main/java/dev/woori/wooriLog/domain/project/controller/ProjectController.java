package dev.woori.wooriLog.domain.project.controller;

import dev.woori.wooriLog.domain.project.dto.request.ProjectCreateReq;
import dev.woori.wooriLog.domain.project.dto.response.ProjectCreateRes;
import dev.woori.wooriLog.domain.project.service.ProjectService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 최신 5개의 프로젝트 조회 (홈화면)
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<?>> getHomeProjectInfos() {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectBasicInfos());
    }

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<BaseResponse<?>> createProject(
            @UserId Long leaderId,
            @Valid @RequestBody ProjectCreateReq request
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                ProjectCreateRes.from(projectService.createProject(leaderId, request))
        );
    }

    // 프로젝트 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse<?>> getProjectInfo(@PathVariable Long projectId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectInfo(projectId));
    }

    // 프로젝트 가입 목록 조회 (유저)
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<?>> getProjectList(@UserId Long memberId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectListByMemberId(memberId));
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<?>> deleteProject(@PathVariable Long projectId, @UserId Long memberId) {
        projectService.deleteProject(projectId, memberId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    // 프로젝트 멤버 추가
    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<BaseResponse<?>> addProjectMember(
            @PathVariable Long projectId,
            @UserId Long memberId,
            Long userId
    ) {
        projectService.addProjectMember(projectId, memberId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    // 프로젝트 멤버 삭제
    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<BaseResponse<?>> deleteProjectMember(
            @PathVariable Long projectId,
            @UserId Long leaderId,
            Long memberId
    ) {
        projectService.deleteProjectMember(projectId, leaderId, memberId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
