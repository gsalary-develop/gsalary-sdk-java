# GSalary SDK for Java

API Document:

- [CN version](https://api.gsalary.com/doc/index.html?lang=cn)
- [EN version](https://api.gsalary.com/doc/index.html?lang=en)

## Import

### Maven
```xml
<dependency>
    <groupId>com.gsalary</groupId>
    <artifactId>gsalary-sdk-java</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle

```text
compile 'com.gsalary:gsalary-sdk-java:1.0.1'
```

## Prepare

Configure your appid, client-side private key, server-side public key and endpoint to the GSalary Client.

The endpoint shall not contain the context path.
```java
import com.gsalary.sdk.GSalaryClient;
import com.gsalary.sdk.GSalaryConnectionConfig;

GSalaryConnectionConfig config = new GSalaryConnectionConfig()
        .setAppid("1111")
        .setClientPrivateKeyFromStream(getClass().getClassLoader().getResourceAsStream("test-private.pem"))
        .setServerPublicKeyFromStream(getClass().getClassLoader().getResourceAsStream("test-public.pem"))
        .setEndpoint("https://api-test.gsalary.com");
GSalaryClient client = new GSalaryClient(config);
```

## Make A Request

### POST request example

```java
String path = "/v1/exchange/quotes";
Map<String, Object> requestBody = new HashMap<>();
requestBody.put("sell_currency", "USD");
requestBody.put("buy_currency", "CNY");
requestBody.put("sell_amount", 0.1);
RawGSalaryRequest request = RawGSalaryRequest.post(path, null, new ObjectMapper().writeValueAsString(requestBody));
String responseContent = client.request(request);
System.out.println(responseContent);
```

### GET request example

```java
String path = "/v1/cards";
Map<String, String> args = new HashMap<>();
args.put("create_start", "2024-02-01T00:00:00+00:00");
args.put("create_end", "2024-05-01T00:00:00+00:00");
args.put("page", "1");
args.put("limit", "20");
RawGSalaryRequest request = RawGSalaryRequest.get(path, args);
String responseContent = client.request(request);
System.out.println(responseContent);
```