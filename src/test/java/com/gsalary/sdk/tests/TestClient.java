package com.gsalary.sdk.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsalary.sdk.GSalaryClient;
import com.gsalary.sdk.GSalaryConnectionConfig;
import com.gsalary.sdk.entity.RawGSalaryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TestClient {
    private GSalaryClient client;

    @BeforeEach
    void setUp() throws IOException {
        GSalaryConnectionConfig config = new GSalaryConnectionConfig()
                .setAppid(System.getenv("GSALARY_SDK_APPID"))
                .setClientPrivateKeyFromStream(Files.newInputStream(Paths.get(System.getenv("GSALARY_SDK_PRIV_KEY"))))
                .setServerPublicKeyFromStream(Files.newInputStream(Paths.get(System.getenv("GSALARY_SDK_PUB_KEY"))))
                .setEndpoint("https://api-test.gsalary.com");

        client = new GSalaryClient(config);
    }

    @Test
    @Disabled
    void testRequestQuote() throws JsonProcessingException {
        String path = "/v1/exchange/quotes";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sell_currency", "USD");
        requestBody.put("buy_currency", "CNY");
        requestBody.put("sell_amount", 0.1);
        RawGSalaryRequest request = RawGSalaryRequest.post(path, null, new ObjectMapper().writeValueAsString(requestBody));
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

    @Test
    @Disabled
    void testListCards() throws JsonProcessingException {
        String path = "/v1/cards";
        Map<String, String> args = new HashMap<>();
        args.put("create_start", "2024-02-01T00:00:00+00:00");
        args.put("create_end", "2024-05-01T00:00:00+00:00");
        args.put("page", "1");
        args.put("limit", "20");
        RawGSalaryRequest request = RawGSalaryRequest.get(path, args);
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

    @Test
    @Disabled
    void testCardProducts(){
        String path = "/v1/card_support/products";
        Map<String, String> args = new HashMap<>();
        args.put("card_type","VIRTUAL");
        RawGSalaryRequest request = RawGSalaryRequest.get(path, args);
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

}
