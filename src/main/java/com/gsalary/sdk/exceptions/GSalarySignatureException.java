package com.gsalary.sdk.exceptions;

public class GSalarySignatureException extends GSalaryException{

    public GSalarySignatureException(String message) {
        super(message);
    }

    public GSalarySignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
