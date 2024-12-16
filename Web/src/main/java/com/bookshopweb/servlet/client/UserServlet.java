package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AuthenticatorDAO;
import com.bookshopweb.dao.CartDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.HashingUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            int countCartItemQuantityByUserId = cartDAO.countCartItemQuantityByUserId(user.getId());
            request.setAttribute("countCartItemQuantity", countCartItemQuantityByUserId);

            int countOrderByUserId = cartDAO.countOrderByUserId(user.getId());
            request.setAttribute("countOrder", countOrderByUserId);

            int countOrderDeliverByUserId = cartDAO.countOrderDeliverByUserId(user.getId());
            request.setAttribute("countOrderDeliver", countOrderDeliverByUserId);

            int countOrderReceivedByUserId = cartDAO.countOrderReceivedByUserId(user.getId());
            request.setAttribute("countOrderReceived", countOrderReceivedByUserId);
        }

        request.getRequestDispatcher("/WEB-INF/views/userView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userDAO.selectByUserName(username);
        if (user == null) {
            response.setStatus(400);
            response.getWriter().write("Username is incorrect!");

        }else{
            if(user.getPassword().equals(password)){
                response.setStatus(200);

                response.getWriter().write("Login success!");
            }else{
                response.setStatus(400);
                response.getWriter().write("Password is incorrect!");
            }
        }

    }
}
