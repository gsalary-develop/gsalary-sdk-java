# GSalary SDK for Java

API文档:

- [中文](https://api.gsalary.com/doc/index.html?lang=cn)
- [英文](https://api.gsalary.com/doc/index.html?lang=en)

## 准备环境

将appid、客户端私钥、服务端公钥、接入域名配置到GSalary Client

接入域名不应该包含路径部分
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

## 请求接口

### POST请求案例

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

### GET请求案例

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