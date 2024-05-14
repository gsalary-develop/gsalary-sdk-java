package com.gsalary.sdk.entity;

import com.gsalary.sdk.secure.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

public class RawGSalaryRequest {
    private final RequestMethod httpMethod;
    private final String path;
    private final Map<String, String> queryArgs;
    private final String requestBody;

    public static RawGSalaryRequest get(String path, Map<String, String> queryArgs) {
        return new RawGSalaryRequest(RequestMethod.GET, path, queryArgs, null);
    }

    public static RawGSalaryRequest delete(String path, Map<String, String> queryArgs) {
        return new RawGSalaryRequest(RequestMethod.DELETE, path, queryArgs, null);
    }

    public static RawGSalaryRequest post(String path, Map<String, String> queryArgs, String requestBody) {
        return new RawGSalaryRequest(RequestMethod.POST, path, queryArgs, requestBody);
    }

    public static RawGSalaryRequest put(String path, Map<String, String> queryArgs, String requestBody) {
        return new RawGSalaryRequest(RequestMethod.PUT, path, queryArgs, requestBody);
    }

    public RawGSalaryRequest(RequestMethod httpMethod, String path, Map<String, String> queryArgs, String requestBody) {
        this.httpMethod = httpMethod;
        this.path = path.startsWith("/") ? path : ("/" + path);
        this.queryArgs = queryArgs;
        this.requestBody = requestBody;
    }

    public String concatPath() {
        StringBuilder pathBuilder = new StringBuilder(path);
        if (this.queryArgs != null && !this.queryArgs.isEmpty()) {
            pathBuilder.append("?");
            for (Map.Entry<String, String> entry : this.queryArgs.entrySet()) {
                pathBuilder.append(entry.getKey()).append("=").append(urlEncode(entry.getValue())).append("&");
            }
            pathBuilder.deleteCharAt(pathBuilder.length() - 1);
            return pathBuilder.toString();
        }
        return path;
    }

    private static String urlEncode(String value) {
        try {
            return value == null ? "" : URLEncoder.encode(value,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            //never happen
            throw new RuntimeException(e);
        }
    }

    public RequestMethod getHttpMethod() {
        return httpMethod;
    }

    public String getBodyHash() {
        if (hasRequestBody()) {
            return Base64.getEncoder().encodeToString(DigestUtils.sha256(requestBody));
        }
        return "";
    }

    public boolean hasRequestBody() {
        return httpMethod.hasBody() && requestBody != null && !requestBody.isEmpty();
    }

    public String getRequestBody() {
        return requestBody;
    }
}
