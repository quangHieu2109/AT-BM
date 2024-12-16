package com.bookshopweb.dao;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.beans.OTP;
import com.bookshopweb.beans.OrderSignature;
import com.bookshopweb.beans.User;
import com.bookshopweb.utils.JDBIUltis;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.jdbi.v3.core.Handle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String URL_STRING = "http://localhost:8080/orderSwing"; // Địa chỉ servlet

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        // Tạo dữ liệu để gửi
        String orderSignatures = "[{\"signature\":\"123\",\"id\":1734238184620}]";
        RequestBody formBody = new FormBody.Builder()
                .add("orderSignatures", orderSignatures)
                .build();

        // Tạo yêu cầu POST
        Request request = new Request.Builder()
                .url(URL_STRING)
                .post(formBody)
                .build();

        try {
            // Gửi yêu cầu và nhận phản hồi
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Request failed: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void testOTP(){
        OTPDAO otpDAO  = new OTPDAO();
        Timestamp timeNow = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        OTP otpFullIn4 = new OTP(1,1,"AVCXUI",timeNow);
        OTP otpNoneID = new OTP(-1,1,"AVCXUI",timeNow);
        System.out.println("Old list OTP" + otpDAO.getAll());
        System.out.println("Add OTP full in4: " + (otpDAO.addOtpFullInfo(otpFullIn4)==1));
        System.out.println("Add OTP none id: " + (otpDAO.addOtpWithoutId(otpNoneID)==1));
        System.out.println("New list OTP: " + otpDAO.getAll());
        System.out.println("Get OTP by user id: " + otpDAO.getByUserId(otpNoneID.getUserId()));
        otpFullIn4.setOtp("QWERTY");
        System.out.println("Get OTP by id: " + otpDAO.getByUserId(otpFullIn4.getId()));
        System.out.println("Remove user id for OTP: "+ (otpDAO.removeByUserId(otpFullIn4.getUserId())==1));
        System.out.println("Get OTP using user id after deleting user id"+ otpDAO.getByUserId(otpFullIn4.getUserId()));
        System.out.println("Update user id for OTP: "+ (otpDAO.updateByOTP(otpFullIn4)==1));

        System.out.println("Get OTP using user id after updating user id"+ otpDAO.getByUserId(otpFullIn4.getUserId()));
    }
    private static void testOrderSignature(){
//        OrderSignatureDAO osd  = new OrderSignatureDAO();
//        Timestamp timeNow = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
//        OrderSignature os = new OrderSignature(1,"AGYGY",timeNow,timeNow,0);
//        System.out.println("Old list OS: "+ osd.getAll());
//        boolean addResult = osd.addOrderSignature(os)==1;
//        System.out.println("Add new OS: " + addResult);
//        System.out.println("New list OS: " + osd.getAll());
//        System.out.println("Get new added item by order id: " + osd.getByOrderId(1));
    }
    private static void testAuth()  {
        AuthenticatorDAO authDAO  = new AuthenticatorDAO();
        Timestamp timeNow = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        Authenticator auth = new Authenticator(1,"this is my time", timeNow, 0, timeNow);
        System.out.println("Old list Auth: "+ authDAO.getAll());
        long id = authDAO.addAuthenticator(auth);
        System.out.println("Add new Auth id: " + id);
        System.out.println("New list Auth: " + authDAO.getAll());
        auth.setId(id);
        auth.setStatus(1);
        System.out.println("Update Auth: " + authDAO.updateStatus(auth));
        System.out.println("Get new added item by user id: " + authDAO.getByUserId(1));
    }
}
