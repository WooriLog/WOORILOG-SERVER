package dev.woori.woorilog.domain.blog.service;

import dev.woori.woorilog.global.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountService {

    private static final String VIEW_COOKIE_NAME = "postView";
    private static final int COOKIE_MAX_AGE = 24 * 60 * 60;

    public boolean shouldIncreaseViewCount(Long blogId, HttpServletRequest request) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, VIEW_COOKIE_NAME);

        if (cookie.isEmpty())
            return true;

        Cookie originalCookie = cookie.get();
        String originalValue = CookieUtils.getDecodedCookieValue(originalCookie);
        String additionalValue = CookieUtils.getCookieValue(String.valueOf(blogId));
        return !CookieUtils.isContainedValue(originalValue, additionalValue);
    }

    public void increaseViewCount(Long blogId, HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> optionalCookie = CookieUtils.getCookie(request, VIEW_COOKIE_NAME);
        log.debug("[Cookie] Client Address : {}", request.getRemoteAddr());
        if (optionalCookie.isEmpty()) {
            System.out.println(true);
            // 새로운 쿠키 추가
            Cookie viewCookie = CookieUtils.createViewCookie(VIEW_COOKIE_NAME, String.valueOf(blogId), COOKIE_MAX_AGE);
            response.addCookie(viewCookie);
        } else {
            // 쿠키 업데이트
            Cookie originalCookie = optionalCookie.get();
            String additionalValue = CookieUtils.getCookieValue(String.valueOf(blogId));
            Cookie updateCookie = CookieUtils.updateCookie(originalCookie, additionalValue, COOKIE_MAX_AGE);
            response.addCookie(updateCookie);
        }
    }
}
