package com.bookshopweb.beans;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Constants {
    public static String GOOGLE_CLIENT_ID = "847546457050-7ccq01atin7m0ke5v2nva6u7i3k7bn6r.apps.googleusercontent.com";
    public static String GOOGLE_CLIENT_SECRET = "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";
    public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/googlehandle";
    public static String GOOGLE_LINK_GET_TOKEN= "https://accounts.google.com/o/oauth2/token";
    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=";
    public static String GOOGLE_GRANT_TYPE = "authorization_code";

    public static String vnp_TmnCode = "EO1BO5ZM";
    public static String vnp_HashSecret = "LLYRBGUYQSWCKJGMWESGOIHBTWMPKMHW";
    public static String vnp_Returnurl = "http://localhost:8080/vnpay_return";
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public static String FACEBOOK_APP_ID = "424799260070203";
    public static String FACEBOOK_APP_SECRET = "dce8cd289e47cbda11f123796c246994";
    public static String FACEBOOK_REDIRECT_URL = "http://localhost:8080/fbhandle";
    public static String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";

    public static String hashAllFields(Map field) throws Exception {
        StringBuilder hashData = new StringBuilder();

        List fieldNames = new ArrayList(field.keySet());
        Collections.sort(fieldNames);
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) field.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {

                    hashData.append('&');
                }
            }
        }
        return hmacSha512(Constants.vnp_HashSecret, hashData.toString());
    }
    private static String hmacSha512(String key, String message) throws Exception {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        sha512Hmac.init(secretKey);
        byte[] hmacBytes = sha512Hmac.doFinal(message.getBytes());
        return bytesToHex(hmacBytes); // Chuyển byte[] thành chuỗi hex
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
