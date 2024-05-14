package com.gsalary.sdk.entity;

public class ErrorInfo {
    private BizResult bizResult;
    private String errorCode;
    private String message;

    public BizResult getBizResult() {
        return bizResult;
    }

    public ErrorInfo setBizResult(BizResult bizResult) {
        this.bizResult = bizResult;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorInfo setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorInfo setMessage(String message) {
        this.message = message;
        return this;
    }
}
