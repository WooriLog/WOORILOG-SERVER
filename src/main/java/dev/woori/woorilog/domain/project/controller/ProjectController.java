package dev.woori.woorilog.domain.project.controller;

import dev.woori.woorilog.domain.project.dto.ProjectDetailInfoDto;
import dev.woori.woorilog.domain.project.dto.request.ProjectCreateReq;
import dev.woori.woorilog.domain.project.dto.response.ProjectCreateRes;
import dev.woori.woorilog.domain.project.dto.response.ProjectHomeRes;
import dev.woori.woorilog.domain.project.dto.response.ProjectInfoRes;
import dev.woori.woorilog.domain.project.service.ProjectService;
import dev.woori.woorilog.global.resolver.UserId;
import dev.woori.woorilog.global.response.ApiResponseUtil;
import dev.woori.woorilog.global.response.BaseResponse;
import dev.woori.woorilog.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 최신 5개의 프로젝트 조회 (홈화면)
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<ProjectHomeRes>> getHomeProjectInfos(@RequestParam(defaultValue = "1") int page) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectBasicInfos(page));
    }

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<BaseResponse<ProjectCreateRes>> createProject(
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
    public ResponseEntity<BaseResponse<ProjectInfoRes>> getProjectInfo(@PathVariable Long projectId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectInfo(projectId));
    }

    // 프로젝트 가입 목록 조회 (유저)
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<ProjectDetailInfoDto>>> getProjectList(@UserId Long memberId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectListByMemberId(memberId));
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<Void>> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    // 프로젝트 멤버 추가
    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<BaseResponse<Void>> addProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        projectService.addProjectMember(projectId, memberId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    // 프로젝트 멤버 삭제
    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<BaseResponse<Void>> deleteProjectMember(
            @PathVariable Long projectId,
            @UserId Long leaderId,
            @PathVariable Long memberId
    ) {
        projectService.deleteProjectMember(projectId, leaderId, memberId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
