package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.service.SendMail;
import com.bookshopweb.utils.HashUtils;
import com.bookshopweb.utils.HashingUtils;
import com.bookshopweb.utils.SignatureUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet(name = "AuthenticatorSwing", value = "/authenticatorSwing")
@MultipartConfig
public class OTPSwingServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
    private final UserDAO userDAO = new UserDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        User user = userDAO.selectByUserName(username);
        if (user == null) {
            response.setStatus(400);
            response.getWriter().write("Username is incorrect!");
        } else {
            AuthenticatorDAO authenticatorDAO = new AuthenticatorDAO();
            JsonArray jsonArray = new JsonArray();
            for(Authenticator authenticator: authenticatorDAO.getAllByUserId(user.getId())){
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(authenticator));
                jsonObject.addProperty("countOrderSignature", authenticatorDAO.getCountSignature(authenticator.getId()));
                jsonArray.add(jsonObject);
            }
            response.getWriter().write(jsonArray.toString());
            response.setStatus(200);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Thiết lập mã hóa cho yêu cầu
        response.setContentType("application/json; charset=UTF-8"); // Thiết lập mã hóa cho phản hồi
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String type = request.getParameter("type");
        User user = userDAO.selectByUserName(username);
        if (user == null) {
            response.setStatus(400);
            response.getWriter().write("Username is incorrect!");

        }else{
            if(!user.getPassword().equals(password)){
                response.setStatus(400);
                response.getWriter().write("Password is incorrect!");

            }else{
//                response.setStatus(200);
                switch (type){
                    case "sendOTP":{
                        sendOTP(request, response, user);
                        break;
                    }
                    case "verifyOTP":{
                        verifyOTP(request, response,user);
                        break;
                    }
                    case "savePublicKey":{
                        savePublicKey(request, response, user);
                        break;
                    }
                }

            }
        }

    }

    protected void sendOTP(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        OTPDAO otpdao = new OTPDAO();
        String otp ="";
        Random rd = new Random();
        for(int i=0; i<6; i++){
            otp += rd.nextInt(10);
        }
        SendMail.sendEmailOtpAuth(user.getEmail(), otp);
        OTP otpObj = new OTP(user.getId(), otp);
        otpdao.addOtpWithoutId(otpObj);
        resp.getWriter().write("OTP has been sent to your email!");

    }
    protected void verifyOTP(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        OTPDAO otpdao = new OTPDAO();
        String otp = req.getParameter("otp");
        OTP userOTP = otpdao.getByUserId(user.getId());
        if(userOTP == null || userOTP.getExpireAt().getTime() <  System.currentTimeMillis()){
            resp.setStatus(400);
            resp.getWriter().write("Your account does not have OTP or OTP has expired, please select the system's OTP sending function!");
        }else{
            if(userOTP.getOtp().equals(otp)){
                userOTP.setStatus(1);
                otpdao.updateByOTP(userOTP);
                resp.setStatus(200);
                resp.getWriter().write("Verify OTP success!");

            }else{
                resp.setStatus(400);
                resp.getWriter().write("OTP is incorrect!");
            }
        }
    }

    protected void savePublicKey(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        OTPDAO otpdao = new OTPDAO();
        AuthenticatorDAO authenticatorDAO = new AuthenticatorDAO();
        OTP userOTP = otpdao.getByUserId(user.getId());
        // Kiểm tra người dùng đã có otp trong db chưa
        if(userOTP == null){
            resp.setStatus(400);
            resp.getWriter().write("Please perform the OTP sending and OTP authentication functions!");
        }else{
            // Kiểm tra OTP đã được xác thực chưa
            if(userOTP.getStatus() ==0){
                resp.setStatus(400);
                resp.getWriter().write("Please verify OTP!");
            }else{
                String publicKey = req.getParameter("publicKey");
                SignatureUtils signatureUtils = new SignatureUtils();
                // Kiểm tra public key có hợp lệ không
                if(!signatureUtils.loadPublicKey(publicKey)){
                    resp.setStatus(400);
                    resp.getWriter().write("Public key is invalid!");
                }else{
                    Authenticator authenticator = new Authenticator();
                    authenticator.setUserId(user.getId());
                    authenticator.setStatus(1);
                    authenticator.setPublicKey(publicKey);
                    authenticator.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    authenticatorDAO.addAuthenticator(authenticator);
                    otpdao.removeByUserId(user.getId());
                    resp.setStatus(200);
                    resp.getWriter().write("Save public key success!");
                }
            }
        }



    }
}
