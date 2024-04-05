package com.bart.test.dto.response;

import com.bart.test.exception.ErrorInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class AppResponse {
    private int code = 200;
    protected String message = "Ok";
    protected Object data;

    public AppResponse(Object data) {
        this.data = data;
    }

    public AppResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AppResponse(ErrorInfo errorInfo) {
        this.code = errorInfo.getCode();
        this.message = errorInfo.getMessage();
    }

    public static AppResponse ok(@Nullable Object data) {
        return new AppResponse(data);
    }

    public static AppResponse ok() {
        return new AppResponse();
    }
    public static AppResponse failed(ErrorInfo errorInfo) {
        return new AppResponse(errorInfo);
    }
}

