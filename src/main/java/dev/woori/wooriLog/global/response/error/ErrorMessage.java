package dev.woori.wooriLog.global.response.error;

public abstract class ErrorMessage {

    /**
     * NOT FOUND - 조회 실패
     */
    public static final String USER_NOT_FOUND = "해당 유저를 찾을 수 없습니다.";
    public static final String PROJECT_NOT_FOUND = "해당 프로젝트를 찾을 수 없습니다.";
    public static final String BLOG_NOT_FOUND = "해당 블로그를 찾을 수 없습니다.";
    public static final String RELATION_NOT_FOUND = "해당 멤버가 가입되지 않은 프로젝트 입니다.";

    /**
     * INVALID - 유효하지 않음
     */
    public static final String INVALID_MEMBER_EMAIL = "멤버의 이메일이 올바르지 않습니다 : ";

}
