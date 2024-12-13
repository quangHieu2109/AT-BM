package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AuthenticatorDAO;
import com.bookshopweb.utils.SignatureUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "VerifyOrderServlet", value = "/verify")
@MultipartConfig
public class VerifyOrderServlet extends HttpServlet {
    AuthenticatorDAO authenticatorDAO = new AuthenticatorDAO();
    SignatureUtils signatureUtils = new SignatureUtils();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String privateKey = req.getParameter("privateKey");
        System.out.println(privateKey);
        int status = 0; // 0 là private key không đúng format, 1 là xác thực không thành oông, 2 thành công
        if (signatureUtils.loadPrivateKey(privateKey)) {//Private key đúng format
            try {
                User user = (User) req.getSession().getAttribute("currentUser");
                signatureUtils.loadPublicKey(authenticatorDAO.getByUserId(user.getId()).getPublicKey());
                if (signatureUtils.authenticate()){// Xác thực thành công
                    status = 2;
                }else {
                    status = 1;
                }
            } catch (Exception e) {
                    status = 1;
                e.printStackTrace();
            }
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status",status);

        PrintWriter out = resp.getWriter();
        out.print(jsonObject.toString());
        out.flush(); // Đảm bảo

    }
}
