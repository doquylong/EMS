package com.bart.test.exception;

public enum ErrorInfo {
    NO_CONTENT (204, "No content"),
    ACCESS_UNAUTHORIZED (401, "Access is unauthorized"),
    ACCESS_DENIED (403, "Access is denied"),
    SYSTEM_ERROR (500, "Internal system error"),
    NOT_FOUND_DATA (9999, "Not found data"),
    EMAIL_ALREADY_EXISTED (1000, "Account already existed"),
    JWT_TOKEN_INVALID(1005,"Jwt invalid"),
    USERNAME_OR_PASS_WRONG(1006,"Username or password invalid"),
    QUESTION_IS_REQUIRED(1007,"Required at least 1 question for active!"),
    CORRECTED_ANSWER_IS_REQUIRED(1008,"Required at least 2 answers and 1 corrected answer!"),
    TIME_OUT(1009,"Beyond the allowed time");
    private final int code;
    private final String message;

    ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }
}
