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
    private static final int MAX_COOKIE_SIZE = 3000;
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
        String decodedValue = getDecodedCookieValue(oldCookie);
        String tempValue = decodedValue + "_" + newValue;
        // 쿠키 오버플로우 방지
        String updateValue = tempValue.length() <= MAX_COOKIE_SIZE ?
                URLEncoder.encode(tempValue, StandardCharsets.UTF_8) : URLEncoder.encode(newValue, StandardCharsets.UTF_8);

        oldCookie.setValue(updateValue);
        oldCookie.setMaxAge(COOKIE_MAX_AGE);
        return oldCookie;
    }

    public static boolean isContainedValue(String originalValue, String newValue) {
        return Arrays.stream(originalValue.split("_")).noneMatch(newValue::equals);
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
