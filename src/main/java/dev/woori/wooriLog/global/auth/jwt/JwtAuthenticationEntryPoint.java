package dev.woori.wooriLog.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.woori.wooriLog.global.auth.Constants;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 미인증 사용자에 대한 응답을 반환하는 JwtAuthenticationEntryPoint
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("401 Unauthorized {}: {}", request.getRequestURI(), authException.getMessage());
        handleException(response);
    }

    private void handleException(HttpServletResponse response) throws IOException {
        setResponse(response, HttpStatus.UNAUTHORIZED);
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.CHARACTER_TYPE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ErrorBaseCode.UNAUTHORIZED));
    }
}