package com.gsalary.sdk.exceptions;

public class GSalaryConnectionException extends GSalaryException{

    public GSalaryConnectionException(String message) {
        super(message);
    }

    public GSalaryConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
