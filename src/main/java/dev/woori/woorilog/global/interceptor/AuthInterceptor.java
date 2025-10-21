package dev.woori.woorilog.global.interceptor;

import dev.woori.woorilog.domain.project.repository.ProjectMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

import static dev.woori.woorilog.domain.DomainConstants.LEADER;
import static dev.woori.woorilog.global.response.error.ErrorMessage.ACCESS_DENIED;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String PATH_VARIABLE = "projectId";
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if ("GET".equals(request.getMethod())) {
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Long)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }

        Long authUserId = (Long) authentication.getPrincipal();

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null && pathVariables.containsKey(PATH_VARIABLE)) {
            Long projectId = Long.parseLong(pathVariables.get(PATH_VARIABLE));

            if (!projectMemberRepository.existsByProjectIdAndMemberIdAndRole(projectId, authUserId, LEADER)) {
                throw new AccessDeniedException(ACCESS_DENIED);
            }
        }

        return true;
    }
}
