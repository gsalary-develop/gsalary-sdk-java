package com.gsalary.sdk.secure;

import java.util.Base64;

public final class Base64Helper {
    private Base64Helper() {
    }

    public static String encode(byte[] bytes, boolean urlSafe) {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        if (urlSafe) {
            base64 = base64.replace('+', '-').replace('/', '_');
            while (base64.endsWith("=")){
                base64 = base64.substring(0, base64.length() - 1);
            }
        }
        return base64;
    }

    public static byte[] decode(String base64) {
        if (base64.contains("-") || base64.contains("_")) {
            base64 = base64.replace('-', '+').replace('_', '/');
        }
        if (base64.length() % 4 != 0) {
            base64 = base64 + "====".substring(base64.length() % 4);
        }
        return Base64.getDecoder().decode(base64);
    }
}
