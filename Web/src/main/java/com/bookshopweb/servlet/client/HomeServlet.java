package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Category;
import com.bookshopweb.beans.Product;
import com.bookshopweb.dao.CategoryDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "")
public class HomeServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = Protector.of(() -> categoryDAO.getPart(12, 0))
                .get(ArrayList::new);
        List<Product> products = Protector.of(() -> productDAO.getOrderedPart(12, 0, "createdAt", "DESC"))
                .get(ArrayList::new);
        request.setAttribute("categories", categories);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/homeView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
