package dev.woori.woorilog.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BaseResponse<T> {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static BaseResponse<Void> of(final ApiCode apiMessage) {
        return BaseResponse.<Void>builder()
                .code(apiMessage.getCode())
                .message(apiMessage.getMessage())
                .build();
    }

    public static <T> BaseResponse<T> of(SuccessCode successCode, T data) {
        return BaseResponse.<T>builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    // Error 메시지 전송시
    public static BaseResponse<Void> of(final int code, final String message) {
        return BaseResponse.<Void>builder()
                .code(code)
                .message(message)
                .build();
    }
}
