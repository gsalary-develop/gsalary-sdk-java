package com.gsalary.sdk;

import com.gsalary.sdk.exceptions.ClientConfigException;
import com.gsalary.sdk.secure.RSAAlgorithm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GSalaryConnectionConfig {
    private String endpoint = "https://api-test.gsalary.com";
    private String appid;
    private PrivateKey clientPrivateKey;
    private PublicKey serverPublicKey;

    public GSalaryConnectionConfig setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public PrivateKey getClientPrivateKey() {
        return clientPrivateKey;
    }

    public PublicKey getServerPublicKey() {
        return serverPublicKey;
    }

    public GSalaryConnectionConfig setClientPrivateKeyString(String clientPrivateKey) {
        return setClientPrivateKeyFromStream(new ByteArrayInputStream(clientPrivateKey.getBytes(StandardCharsets.UTF_8)));
    }

    public GSalaryConnectionConfig setServerPublicKeyString(String serverPublicKey) {
        return setServerPublicKeyFromStream(new ByteArrayInputStream(serverPublicKey.getBytes(StandardCharsets.UTF_8)));
    }

    public GSalaryConnectionConfig setClientPrivateKeyFromStream(InputStream stream) {
        try (InputStream in = stream) {
            this.clientPrivateKey = RSAAlgorithm.parsePrivateKey(in);
        } catch (IOException e) {
            throw new ClientConfigException("Failed to load private key", e);
        }
        return this;
    }

    public GSalaryConnectionConfig setServerPublicKeyFromStream(InputStream stream) {
        try (InputStream in = stream) {
            this.serverPublicKey = RSAAlgorithm.parsePublicKey(in);
        } catch (IOException e) {
            throw new ClientConfigException("Failed to load public key", e);
        }
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public GSalaryConnectionConfig setEndpoint(String endpoint) {
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        this.endpoint = endpoint;
        return this;
    }

}
