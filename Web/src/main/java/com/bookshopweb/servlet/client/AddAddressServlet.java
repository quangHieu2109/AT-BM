package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Address;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AddressDAO;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddAddressServlet", value = "/addAddress")
public class AddAddressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/address.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");

        if (user != null) {
            AddressDAO addressDAO = new AddressDAO();
           int res = addressDAO.insertAddress(new Address(0l, user.getId(),  req.getParameter("province"), req.getParameter("district"), req.getParameter("ward"),req.getParameter("houseNumber")));

        }
    }
}
