package com.bookshopweb.servlet.admin.product;

import com.bookshopweb.beans.Category;
import com.bookshopweb.beans.Product;
import com.bookshopweb.dao.CategoryDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ProductDetailServlet", value = "/admin/productManager/detail")
public class ProductDetailServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<Product> productFromServer = Protector.of(() -> productDAO.selectPrevalue(id)).get();

        if (productFromServer.isPresent()) {
            Product product = productFromServer.get();
            product.setDescription(TextUtils.toParagraph(Optional.ofNullable(product.getDescription()).orElse("")));

            Optional<Category> categoryFromServer = Protector.of(() -> categoryDAO.getByProductId(id)).get(Optional::empty);

            request.setAttribute("product", product);
            request.setAttribute("category", categoryFromServer.orElseGet(Category::new));
            request.getRequestDispatcher("/WEB-INF/views/productDetailView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/productManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
