package com.gsalary.sdk.exceptions;

import com.gsalary.sdk.entity.BizResult;

public class GSalaryServiceException extends GSalaryException {
    private final int httpStatus;
    private final BizResult bizResult;
    private final String errorCode;
    private final String errorMessage;

    public GSalaryServiceException(int httpStatus, BizResult bizResult, String errorCode, String errorMessage) {
        super(String.format("GSalary response error: [%s] %s - %s", bizResult, errorCode, errorMessage));
        this.httpStatus = httpStatus;
        this.bizResult = bizResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizResult getBizResult() {
        return bizResult;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
