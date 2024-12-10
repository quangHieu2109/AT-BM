package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.User;
import com.bookshopweb.dao.CartDAO;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
