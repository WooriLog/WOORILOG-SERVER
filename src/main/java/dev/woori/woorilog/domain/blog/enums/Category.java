package dev.woori.woorilog.domain.blog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    CHECKPOINT("체크포인트"),
    REVIEW("회고"),
    TROUBLESHOOTING("트러블슈팅"),
    DEEPDIVE("딥다이브"),
    ETC("기타");

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
