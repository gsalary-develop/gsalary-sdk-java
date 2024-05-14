package com.gsalary.sdk.entity;

public enum RequestMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true);
    private final boolean hasBody;

    RequestMethod(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public boolean hasBody() {
        return hasBody;
    }
}
