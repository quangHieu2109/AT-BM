package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Category;
import com.bookshopweb.beans.Product;
import com.bookshopweb.dao.CategoryDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AllCategoryServlet", value = "/categoryViewAll")
public class AllCategoryServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Category> categories = Protector.of(() -> categoryDAO.getAll())
                .get(ArrayList::new);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/categoryViewAll.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
