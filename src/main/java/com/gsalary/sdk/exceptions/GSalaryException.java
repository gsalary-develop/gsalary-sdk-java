package com.gsalary.sdk.exceptions;

public class GSalaryException extends RuntimeException {
    public GSalaryException() {
    }

    public GSalaryException(String message) {
        super(message);
    }

    public GSalaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
