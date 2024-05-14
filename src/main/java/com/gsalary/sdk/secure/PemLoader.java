package com.gsalary.sdk.secure;

import com.gsalary.sdk.exceptions.ClientConfigException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class PemLoader {
    private PemLoader() {
    }

    public static byte[] load(InputStream pemStream) {
        try (InputStreamReader reader = new InputStreamReader(pemStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("-----")) {
                    continue;
                }
                result.append(line);
            }
            return Base64.getDecoder().decode(result.toString());
        } catch (IOException e) {
            throw new ClientConfigException("Failed to load pem certificate", e);
        }
    }
}
