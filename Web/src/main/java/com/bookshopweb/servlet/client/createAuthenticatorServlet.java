package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.OTP;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.OTPDAO;
import com.bookshopweb.service.SendMail;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
}
