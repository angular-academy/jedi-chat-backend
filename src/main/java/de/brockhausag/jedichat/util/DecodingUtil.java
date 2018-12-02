package de.brockhausag.jedichat.util;

import java.util.Base64;

public class DecodingUtil {
    public static byte[] base64ToByeArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static String byteArrayToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
