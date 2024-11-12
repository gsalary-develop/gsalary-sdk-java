package com.gsalary.sdk.secure;

import com.gsalary.sdk.exceptions.ClientConfigException;
import com.gsalary.sdk.exceptions.GSalarySignatureException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class RSAAlgorithm {
    private RSAAlgorithm() {
    }

    public static PrivateKey parsePrivateKey(InputStream in) {
        byte[] keyBytes = PemLoader.load(in);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ClientConfigException("Failed to resolve private key", e);
        }
    }

    public static PublicKey parsePublicKey(InputStream in) {
        byte[] keyBytes = PemLoader.load(in);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ClientConfigException("Failed to resolve public key", e);
        }
    }

    public static String signature(PrivateKey privKey, String signContent) {
        try {
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privKey);
            signature.update(signContent.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return Base64Helper.encode(signed, false);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new GSalarySignatureException("Failed to sign content", e);
        }
    }

    public static boolean verifySignature(PublicKey pubKey, String signContent, String sign) {
        try {
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            signature.update(signContent.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64Helper.decode(sign));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new GSalarySignatureException("Failed to sign content", e);
        }
    }
}
