package dev.woori.wooriLog.domain.blog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProgressValue {
    // 회고
    OUTLINE("개요"),
    KEEP("잘했던 점"),
    PROBLEM("아쉬웠던 점"),
    TRY("배운 점 & 개선 방안"),
    // 트러블 슈팅
    SITUATION("상황"),
    CONCERN("고민"),
    ACTION("실행"),
    REFLECTION("회고/성장"),
    // 딥다이브
    INTRODUCE("기술소개"),
    EXAMPLE("예시"),
    CORE("핵심 개념"),
    CONCLUSION("결론"),
    // 기타
    SUMMARY("요약");

    private final String value;

    ProgressValue(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProgressValue fromValue(String value) {
        for (ProgressValue category : ProgressValue.values()) {
            if (category.value.equals(value) || category.name().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("정의되지 않은 값입니다." + value);
    }
}
