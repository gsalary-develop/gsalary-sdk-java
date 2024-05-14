package com.gsalary.sdk.secure;

import com.gsalary.sdk.exceptions.GSalarySignatureException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {
    private DigestUtils() {

    }

    public static byte[] sha256(byte[] data){
        return makeHash(data,"SHA-256");
    }

    public static byte[] sha256(String data){
        return sha256(data.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] makeHash(byte[] data, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new GSalarySignatureException("Failed to calculate hash value", e);
        }
    }
}
