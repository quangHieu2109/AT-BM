package com.bookshopweb.dao;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.beans.OTP;
import com.bookshopweb.beans.OrderSignature;
import com.bookshopweb.beans.User;
import com.bookshopweb.utils.JDBIUltis;
import com.bookshopweb.utils.SignatureUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jdbi.v3.core.Handle;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws SQLException {
//        UserJDBI userJDBI = JDBIUltis.getJDBI().onDemand(UserJDBI.class);
//        List<User> users = userJDBI.selectAll(); // lấy dữ liệu thông qua map constructor
//        List<User> users = JDBIUltis.getJDBI().withHandle( handle ->
//                handle.createQuery("select * from user").map(new UserMapper())
//                        .list()); // lấy dữ liệu thông qua mapper object
//        System.out.println(new OrderItemDAO().getTotalPriceByOrderId(1));
//        testOrderSignature();
//        testOTP();
        testAuth();

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
        SignatureUtils signatureUtils = new SignatureUtils();
        String publicKey = "";
        try {
            signatureUtils.genKey();
            publicKey = signatureUtils.getPublicKeyBase64();
            System.out.println(signatureUtils.getPrivateKeyBase64());
        } catch (Exception e){
            throw e;
        }
        AuthenticatorDAO authDAO  = new AuthenticatorDAO();
        Timestamp timeNow = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        Authenticator auth = new Authenticator(1,publicKey, timeNow, 0, timeNow);
//        System.out.println("Old list Auth: "+ authDAO.getAll());
        long id = authDAO.addAuthenticator(auth);
        System.out.println("Add new Auth id: " + id);
//        System.out.println("New list Auth: " + authDAO.getAll());
//        auth.setId(id);
//        auth.setStatus(1);
//        System.out.println("Update Auth: " + authDAO.updateStatus(auth));
//        System.out.println("Get new added item by user id: " + authDAO.getByUserId(1));
    }
}
