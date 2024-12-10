package com.bookshopweb.servlet.admin.productreview;

import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.dao.ProductReviewDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ProductReviewDetailServlet", value = "/admin/reviewManager/detail")
public class ProductReviewDetailServlet extends HttpServlet {
    private final ProductReviewDAO productReviewDAO = new ProductReviewDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<ProductReview> productReviewFromServer = Protector.of(() -> productReviewDAO.selectPrevalue(id))
                .get();

        if (productReviewFromServer.isPresent()) {
            ProductReview productReview = productReviewFromServer.get();
            productReview.setContent(TextUtils.toParagraph(productReview.getContent()));
            Protector.of(() -> userDAO.selectPrevalue(productReview.getUserId())).get()
                    .ifPresent(productReview::setUser);
            Protector.of(() -> productDAO.selectPrevalue(productReview.getProductId())).get()
                    .ifPresent(productReview::setProduct);
            request.setAttribute("productReview", productReview);
            request.getRequestDispatcher("/WEB-INF/views/productReviewDetailView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
