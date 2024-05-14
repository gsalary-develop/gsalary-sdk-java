package com.gsalary.sdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gsalary.sdk.entity.ErrorInfo;
import com.gsalary.sdk.entity.RawGSalaryRequest;
import com.gsalary.sdk.entity.SignatureHeader;
import com.gsalary.sdk.exceptions.GSalaryConnectionException;
import com.gsalary.sdk.exceptions.GSalaryServiceException;
import com.gsalary.sdk.exceptions.GSalarySignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GSalaryClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GSalaryConnectionConfig connConfig;
    private final GSalarySignatureHelper signatureHelper;
    private final ObjectMapper objectMapper;

    public GSalaryClient(GSalaryConnectionConfig connConfig) {
        this.connConfig = connConfig;
        this.signatureHelper = new GSalarySignatureHelper(connConfig);
        this.objectMapper = createObjectMapper();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public String request(RawGSalaryRequest request) {
        SignatureHeader signatureHeader = signatureHelper.sign(request);
        String requestUrl = connConfig.getEndpoint() + request.concatPath();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getHttpMethod().name());
            connection.setRequestProperty("Authorization", signatureHeader.headerValue());
            connection.setRequestProperty("x-appid", connConfig.getAppid());
            connection.setRequestProperty("Accept", "application/json");
            if (request.hasRequestBody()) {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                try (OutputStream requestOutput = connection.getOutputStream()) {
                    requestOutput.write(request.getRequestBody().getBytes(StandardCharsets.UTF_8));
                    requestOutput.flush();
                }
            }
            try {
                int responseCode = connection.getResponseCode();
                String authorization = connection.getHeaderField("Authorization");
                String responseContent = readResponse(connection);
                if (logger.isDebugEnabled()) {
                    logger.debug("Request URL: {},  Response status: {}; content: {}", requestUrl, responseCode, responseContent);
                }
                if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                    ErrorInfo errorInfo = objectMapper.readValue(responseContent, ErrorInfo.class);
                    throw new GSalaryServiceException(responseCode, errorInfo.getBizResult(), errorInfo.getErrorCode(), errorInfo.getMessage());
                }
                if (!signatureHelper.verify(request.getHttpMethod().name(), request.concatPath(), responseContent, authorization)) {
                    throw new GSalarySignatureException("Failed to verify response signature");
                }
                return responseContent;
            }catch (IOException e){
                int httpCode = connection.getResponseCode();
                String errorContent = readErrorResponse(connection);
                ErrorInfo errorInfo = objectMapper.readValue(errorContent, ErrorInfo.class);
                throw new GSalaryServiceException(httpCode, errorInfo.getBizResult(), errorInfo.getErrorCode(), errorInfo.getMessage());
            }
        } catch (IOException e) {
            throw new GSalaryConnectionException("Connection to gsalary failed", e);
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        try (InputStream responseInput = connection.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int readLen;
            while ((readLen = responseInput.read(buffer)) != -1) {
                bos.write(buffer, 0, readLen);
            }
            bos.flush();
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static String readErrorResponse(HttpURLConnection connection) throws IOException {
        try (InputStream responseInput = connection.getErrorStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int readLen;
            while ((readLen = responseInput.read(buffer)) != -1) {
                bos.write(buffer, 0, readLen);
            }
            bos.flush();
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
