package dev.woori.woorilog.global.response.error;

public abstract class ErrorMessage {

    /**
     * BAD REQUEST
     */
    public static final String LEADER_CAN_NOT_DELETE = "팀장을 프로젝트에서 삭제할 수 없습니다.";

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
    public static final String DUPLICATED_REQUEST = "이미 존재하는 리소스 입니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";

    /**
     * DENIED - 접근 거부
     */
    public static final String BLOG_ACCESS_DENIED = "블로그 정보를 변경할 권한이 없습니다.";
    public static final String ACCESS_DENIED = "권한이 없습니다.";
}
