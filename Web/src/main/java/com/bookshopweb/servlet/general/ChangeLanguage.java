package com.bookshopweb.servlet.general;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@WebServlet("/changeLanguage")
public class ChangeLanguage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        HttpSession session = req.getSession();
        if (session.getAttribute("lang") != null && session.getAttribute("lang").toString().equals("en")) {
            session.setAttribute("lang", "vi");
        }else{
            session.setAttribute("lang", "en");
        }
        System.out.println(url);
    }
}
