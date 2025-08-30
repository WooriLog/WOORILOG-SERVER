package dev.woori.wooriLog.domain.blog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProgressValue {
    SITUATION("상황"),
    CONCERN("고민"),
    ACTION("실행"),
    REFLECTION("회고/성장");

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
