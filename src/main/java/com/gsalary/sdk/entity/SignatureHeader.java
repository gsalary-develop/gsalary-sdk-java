package com.gsalary.sdk.entity;

import com.gsalary.sdk.exceptions.GSalarySignatureException;
import com.gsalary.sdk.tools.StringTools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SignatureHeader {
    private String algorithm;
    private String time;
    private String signature;

    public static SignatureHeader parse(String headerValue, boolean ignoreExpire) {
        Map<String, String> args = Arrays.stream(headerValue.split(","))
                .map(String::trim)
                .filter(kvPair -> kvPair.contains("="))
                .map(kvPair -> {
                    String key = StringTools.substringBefore(kvPair, "=");
                    String value = StringTools.substringAfter(kvPair, "=");
                    return new AbstractMap.SimpleEntry<>(key, urlDecode(value));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> replacement, HashMap::new));

        String alg = args.get("algorithm");
        String timeStr = args.get("time");
        String sign = args.get("signature");
        if (StringTools.isAnyEmpty(alg, timeStr, sign)) {
            throw new GSalarySignatureException("Incorrect authentication");
        }
        if (!ignoreExpire && Duration.between(Instant.now(), Instant.ofEpochMilli(Long.decode(timeStr))).abs().compareTo(Duration.ofMinutes(5)) > 0) {
            throw new GSalarySignatureException("Signature time expired");
        }
        if (!"RSA2".equals(alg)) {
            throw new GSalarySignatureException("Incorrect algorithm");
        }
        return new SignatureHeader(alg, timeStr, sign);
    }

    public SignatureHeader(String algorithm, String time, String signature) {
        this.algorithm = algorithm;
        this.time = time;
        this.signature = signature;
    }

    public String headerValue() {
        return String.format("algorithm=%s,time=%s,signature=%s", algorithm, time, urlEncode(signature));
    }

    public byte[] signatureBytes() {
        return Base64.getDecoder().decode(signature);
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SignatureHeader setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public SignatureHeader setTime(String time) {
        this.time = time;
        return this;
    }

    public SignatureHeader setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getTime() {
        return time;
    }

    public String getSignature() {
        return signature;
    }
}
