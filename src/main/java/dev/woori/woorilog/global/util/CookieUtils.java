package dev.woori.woorilog.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    private static final int COOKIE_MAX_AGE = 24 * 60 * 60; // 24시간
    private static final String NAME_PREFIX = "[";
    private static final String NAME_POSTFIX = "]";

    public static final String VIEW_COOKIE_NAME = "postView";

    public static Cookie createViewCookie(String name, String value) {
        String cookieValue = getCookieValue(value);
        Cookie cookie = new Cookie(name, URLEncoder.encode(cookieValue, StandardCharsets.UTF_8));
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static String getCookieValue(String value) {
        return NAME_PREFIX + value + NAME_POSTFIX;
    }

    public static String getDecodedCookieValue(Cookie cookie) {
        return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
    }

    public static Cookie updateCookie(Cookie oldCookie, String newValue) {
        String updateValue = oldCookie.getValue() + "_" + newValue;
        oldCookie.setValue(URLEncoder.encode(updateValue, StandardCharsets.UTF_8));
        oldCookie.setPath("/");
        oldCookie.setMaxAge(COOKIE_MAX_AGE);
        return oldCookie;
    }

    // 쿠키 조회
    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst();
    }
}
