package letsdev.core.password.encoder.adapter;

import letsdev.core.password.exception.PasswordEncoderGenerationException;

class ModifiedBase64 {
    private static final char[] BASE64_CODE =
            "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    static String encodeAlternativeBase64(byte[] d) {
        int off = 0;
        int len = d.length;
        StringBuilder rs = new StringBuilder();

        if (len == 0) {
            throw new PasswordEncoderGenerationException("");
        }

        while(off < len) {
            int c1 = d[off++] & 255;
            rs.append(BASE64_CODE[c1 >> 2 & 63]);
            c1 = (c1 & 3) << 4;
            if (off >= len) {
                rs.append(BASE64_CODE[c1 & 63]);
                break;
            }

            int c2 = d[off++] & 255;
            c1 |= c2 >> 4 & 15;
            rs.append(BASE64_CODE[c1 & 63]);
            c1 = (c2 & 15) << 2;
            if (off >= len) {
                rs.append(BASE64_CODE[c1 & 63]);
                break;
            }

            c2 = d[off++] & 255;
            c1 |= c2 >> 6 & 3;
            rs.append(BASE64_CODE[c1 & 63]);
            rs.append(BASE64_CODE[c2 & 63]);
        }
        return rs.toString();
    }
}
