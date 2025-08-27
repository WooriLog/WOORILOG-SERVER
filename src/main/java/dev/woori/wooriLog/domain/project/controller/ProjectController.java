package dev.woori.wooriLog.domain.project.controller;

import dev.woori.wooriLog.domain.project.dto.ProjectCreateReq;
import dev.woori.wooriLog.domain.project.service.ProjectService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<?> createProject(
            @UserId Long leaderId,
            @RequestBody ProjectCreateReq request
    ) {
        projectService.createProject(leaderId, request);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<?> getProjectInfo(@PathVariable Long projectId) {
        return ApiResponseUtil.success(SuccessCode.OK, projectService.getProjectInfo(projectId));
    }
}
