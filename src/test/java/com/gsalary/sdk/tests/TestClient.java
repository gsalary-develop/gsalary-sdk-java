package com.gsalary.sdk.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsalary.sdk.GSalaryClient;
import com.gsalary.sdk.GSalaryConnectionConfig;
import com.gsalary.sdk.entity.RawGSalaryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
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
//    @Disabled
    void testPayoutQuote() throws JsonProcessingException {
        String path = "/v1/remittance/quotes";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("payee_account_id", "2025010602133828914600412602");
        requestBody.put("payer_id", "2025010602285686414600412602");
        requestBody.put("purpose", "SALARY");
        requestBody.put("pay_currency", "USD");
        requestBody.put("receive_currency", "CNY");
        requestBody.put("amount", 100);
        requestBody.put("amount_type", "RECEIVE_AMOUNT");
        RawGSalaryRequest request = RawGSalaryRequest.post(path, null, new ObjectMapper().writeValueAsString(requestBody));
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

    @Test
    @Disabled
    public void testAddPayee() throws JsonProcessingException {
        String path = "/v1/remittance/payees";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subject_type", "INDIVIDUAL");
        requestBody.put("account_type", "E_WALLET");
        requestBody.put("country", "CN");
        requestBody.put("first_name", "LI");
        requestBody.put("last_name", "LEI");
        Map<String, String> mobile = new HashMap<>();
        mobile.put("nation_code", "86");
        mobile.put("mobile", "13811111111");
        requestBody.put("mobile", mobile);
        requestBody.put("currency", "CNY");
        RawGSalaryRequest request = RawGSalaryRequest.post(path, null, new ObjectMapper().writeValueAsString(requestBody));
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

    @Test
    @Disabled
    public void testAddPayeeAccount() throws JsonProcessingException {
        String path = "/v1/remittance/payees/2025010602101156714600412601/accounts";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("payment_method", "ALIPAY");
        requestBody.put("account_no", "86-13721473389");
        RawGSalaryRequest request = RawGSalaryRequest.post(path, null, new ObjectMapper().writeValueAsString(requestBody));
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

    @Test
    @Disabled
    public void testAddPayer() throws JsonProcessingException{
        String path = "/v1/remittance/payers";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subject_type", "INDIVIDUAL");
        requestBody.put("first_name", "David");
        requestBody.put("last_name", "Wang");
        requestBody.put("cert_type","PASSPORT");
        requestBody.put("cert_number","123456789");
        requestBody.put("region", "AU");
        requestBody.put("birthday","2000-01-01");
        requestBody.put("cert_files",new String[]{"R0VFS1NFQ1VSRQAAAAEAAAAMGVFC05HyYxKgbucjDZVwj8rB88uEkk2+gECg18b62L2a2j3ns/LbmmlEUt1dS6Vfq9edT1aMOtg9H5vMy+4ZOtIHwp8wZAeFTWn33qAhuPTYz/5q8HeCvGqZGf6OoS0+jECO9/rF6JtMowbMISMa1tdn5Kp5tvI/LX3yJvgBf90d016IIDXPcvVWy4ywnxkjW54IG3VlpGQ70qz50eZYUKla8DgJfKY="});
        Map<String,Object> address = new HashMap<>();
        address.put("country", "AU");
        address.put("state", "NSW");
        address.put("city", "Sydney");
        address.put("street", "123456");
        address.put("postcode", "2000");
        requestBody.put("address", address);
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
    void testCardProducts() {
        String path = "/v1/card_support/products";
        Map<String, String> args = new HashMap<>();
        args.put("card_type", "VIRTUAL");
        RawGSalaryRequest request = RawGSalaryRequest.get(path, args);
        String responseContent = client.request(request);
        System.out.println(responseContent);
    }

}
