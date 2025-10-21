package dev.woori.woorilog.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* dev.woori.woorilog.domain..controller.*.*(..))")
    private void onRequest() {};

    @Pointcut("execution(* dev.woori.woorilog.domain..service.*.*(..))")
    private void onService() {};

    @Pointcut("execution(* dev.woori.woorilog.global.auth.service.GoogleOAuthService.*(..))")
    private void onLogin() {};

    @Before("onRequest()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("[{}] {} {}",clazz.getSimpleName(), request.getMethod(), request.getRequestURI());
    }

    @Before("onService()")
    public void beforeServiceLog(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("[{}] {}() called", className, methodName);

        if (args.length > 0) {
            log.debug("[{}] Parameters: {}", className, Arrays.toString(args));
        }
    }

    @Before("onLogin()")
    public void beforeLoginLog(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[GoogleOAuthService] {}() called", methodName);
    }
}
