package dev.woori.woorilog.domain.blog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static dev.woori.woorilog.global.response.error.ErrorMessage.NOT_DEFINED_VALUE;

public enum ProgressValue {
    // 회고
    OUTLINE("개요"),
    KEEP("유지할 것"),
    PROBLEM("개선할 것"),
    TRY("시도할 것"),
    // 트러블 슈팅
    SITUATION("문제상황"),
    CONCERN("고민"),
    ACTION("실행"),
    REFLECTION("결론&회고"),
    // 딥다이브
    INTRODUCE("기술 소개"),
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
        throw new IllegalArgumentException(NOT_DEFINED_VALUE + value);
    }
}
