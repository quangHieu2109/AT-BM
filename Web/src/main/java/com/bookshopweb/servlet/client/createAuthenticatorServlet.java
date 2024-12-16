package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.beans.OTP;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.OTPDAO;
import com.bookshopweb.service.SendMail;
import com.bookshopweb.utils.SignatureUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;
import java.util.Random;

@WebServlet(name = "CreateAuthenticatorServlet", value = "/createAuthenticator")
@MultipartConfig
public class createAuthenticatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession();
//        OTPDAO otpdao = new OTPDAO();
//        User user = (User) session.getAttribute("currentUser");
//        OTP otp = otpdao.getByUserId(user.getId());
//        if(otp ==null || otp.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
//            sendOTP(req, resp);
//        }
        req.getRequestDispatcher("/WEB-INF/views/createAuthenticatorView.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        System.out.println(type);
        switch (type){
            case "resendOTP": {
                sendOTP(req, resp);
                break;
            }
        }
        resp.setStatus(200);
    }
    protected void sendOTP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        OTPDAO otpdao = new OTPDAO();
        User user = (User) session.getAttribute("currentUser");
        String otp ="";
        Random rd = new Random();
        for(int i=0; i<6; i++){
            otp += rd.nextInt(10);
        }
        SendMail.sendEmailOtpAuth(user.getEmail(), otp);
        OTP otpObj = new OTP(user.getId(), otp);
        otpdao.addOtpWithoutId(otpObj);
    }
    protected void verifyOTP_Swing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        OTPDAO otpdao = new OTPDAO();
        User user = (User) session.getAttribute("currentUser");
        String otp = req.getParameter("otp");
        OTP userOTP = otpdao.getByUserId(user.getId());
        if(userOTP == null || userOTP.getExpireAt().getTime() >  System.currentTimeMillis()){
            resp.setStatus(400);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write("Your account does not have OTP or OTP has expired, please select the system's OTP sending function!");
        }else{
            if(userOTP.getOtp().equals(otp)){
                resp.setStatus(200);
            }else{
                resp.setStatus(400);
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("application/json");
                resp.getWriter().write("OTP is incorrect!");
            }
        }
    }
    protected void verifyOTP_Web(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        HttpSession session = req.getSession();
        OTPDAO otpdao = new OTPDAO();
        User user = (User) session.getAttribute("currentUser");
        String otp = req.getParameter("otp");
        OTP userOTP = otpdao.getByUserId(user.getId());
        if(userOTP == null || userOTP.getExpireAt().getTime() <  System.currentTimeMillis()){
            resp.setStatus(400);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write("Your account does not have OTP or OTP has expired, please select the system's OTP sending function!");
        }else{
            if(userOTP.getOtp().equals(otp)){
                SignatureUtils signatureUtils = new SignatureUtils();
                signatureUtils.genKey();
                Authenticator authenticator = new Authenticator();

                resp.setStatus(200);
            }else{
                resp.setStatus(400);
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("application/json");
                resp.getWriter().write("OTP is incorrect!");
            }
        }
    }
}
