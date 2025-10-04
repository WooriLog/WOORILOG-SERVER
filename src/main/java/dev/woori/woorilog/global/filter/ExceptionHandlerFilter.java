package dev.woori.woorilog.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.woori.woorilog.global.auth.Constants;
import dev.woori.woorilog.global.exception.JwtTokenException;
import dev.woori.woorilog.global.response.BaseResponse;
import dev.woori.woorilog.global.response.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleUnauthorizedException(response, e);
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        JwtTokenException jwtException = (JwtTokenException) e;
        ErrorCode errorCode = jwtException.getErrorCode();
        HttpStatus httpStatus = errorCode.getHttpStatus();
        setResponse(response, httpStatus, errorCode);
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus, ErrorCode errorBaseCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.CHARACTER_TYPE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(BaseResponse.of(errorBaseCode)));
    }
}

