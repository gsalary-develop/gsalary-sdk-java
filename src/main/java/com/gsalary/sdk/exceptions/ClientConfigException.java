package com.gsalary.sdk.exceptions;

public class ClientConfigException extends GSalaryException {

    public ClientConfigException(String message) {
        super(message);
    }

    public ClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
