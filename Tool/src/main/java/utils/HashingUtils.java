package utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {
    public static String hash(String s) {
        s+="@#$%^%$#*&^";
        String hashed = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] bytes = s.getBytes("UTF-8");
            byte[] digest = md.digest(bytes);
            BigInteger re = new BigInteger(1, digest);
            hashed =  re.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashed;
    }
}
