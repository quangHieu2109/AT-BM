package com.bookshopweb.servlet.client;

import com.bookshopweb.utils.SignatureUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

@WebServlet(name = "AuthenticatorServlet", value = "/authenticator")
@MultipartConfig
public class AuthenticatorServlet extends HttpServlet {
    SignatureUtils signatureUtils = new SignatureUtils();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/authenticatorView.jsp").forward(req, resp);
    }
}
