package dev.woori.wooriLog.domain.blog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    BLOG("블로그"),
    CHECKPOINT("체크포인트"),
    REVIEW("리뷰"),
    TECH("기술/딥다이브");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category category(String value) {
        for (Category category : Category.values()) {
            if (category.value.equals(value) || category.name().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("정의되지 않은 값입니다." + value);
    }
}
