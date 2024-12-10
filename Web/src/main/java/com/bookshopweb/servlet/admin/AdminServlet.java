package com.bookshopweb.servlet.admin;

import com.bookshopweb.dao.CategoryDAO;
import com.bookshopweb.dao.OrderDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalUsers = Protector.of(userDAO::count).get(0);
        int totalCategories = Protector.of(categoryDAO::count).get(0);
        int totalProducts = Protector.of(productDAO::count).get(0);
        int totalOrders = Protector.of(orderDAO::count).get(0);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalCategories", totalCategories);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.getRequestDispatcher("/WEB-INF/views/adminView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
