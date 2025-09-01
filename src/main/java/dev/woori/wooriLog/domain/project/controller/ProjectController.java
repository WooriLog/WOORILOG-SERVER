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
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/projects/home")
    public ResponseEntity<BaseResponse<?>> getHomeProjectInfos() {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectBasicInfos());
    }

    @PostMapping("/projects")
    public ResponseEntity<BaseResponse<?>> createProject(
            @UserId Long leaderId,
            @Valid @RequestBody ProjectCreateReq request
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                ProjectCreateRes.from(projectService.createProject(leaderId, request))
        );
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<BaseResponse<?>> getProjectInfo(@PathVariable Long projectId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectInfo(projectId));
    }

    @GetMapping("/projects/list")
    public ResponseEntity<BaseResponse<?>> getProjectList(@UserId Long memberId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectListByMemberId(memberId));
    }
}
