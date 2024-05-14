package com.gsalary.sdk;

import com.gsalary.sdk.entity.RawGSalaryRequest;
import com.gsalary.sdk.entity.SignatureHeader;
import com.gsalary.sdk.secure.DigestUtils;
import com.gsalary.sdk.secure.RSAAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Base64;

public final class GSalarySignatureHelper {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GSalaryConnectionConfig connConfig;

    public GSalarySignatureHelper(GSalaryConnectionConfig connConfig) {
        this.connConfig = connConfig;
    }

    public SignatureHeader sign(RawGSalaryRequest request) {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String signBase = String.format("%s %s%n%s%n%s%n%s%n", request.getHttpMethod(), request.concatPath(),
                connConfig.getAppid(),
                timestamp,
                request.getBodyHash());
        if (logger.isDebugEnabled()) {
            logger.debug("Signature Bases: {}", signBase);
        }
        String signature = RSAAlgorithm.signature(connConfig.getClientPrivateKey(), signBase);
        return new SignatureHeader("RSA2", timestamp, signature);
    }

    public boolean verify(String method, String pathWithQuery, String body, String authorizationHeader) {
        String bodyHash = Base64.getEncoder().encodeToString(DigestUtils.sha256(body));
        SignatureHeader header = SignatureHeader.parse(authorizationHeader);
        String signBase = String.format("%s %s%n%s%n%s%n%s%n", method, pathWithQuery,
                connConfig.getAppid(),
                header.getTime(),
                bodyHash);
        if (logger.isDebugEnabled()) {
            logger.debug("Verify Signature Bases: {}", signBase);
        }
        return RSAAlgorithm.verifySignature(connConfig.getServerPublicKey(), signBase, header.getSignature());
    }
}
