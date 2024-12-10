package com.bookshopweb.servlet.admin.productreview;

import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.dao.ProductReviewDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ProductReviewManagerServlet", value = "/admin/reviewManager")
public class ProductReviewManagerServlet extends HttpServlet {
    private final ProductReviewDAO productReviewDAO = new ProductReviewDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();

    private static final int PRODUCT_REVIEWS_PER_PAGE = 25;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalProductReviews = Protector.of(productReviewDAO::count).get(0);
        int totalPages = totalProductReviews / PRODUCT_REVIEWS_PER_PAGE +
                (totalProductReviews % PRODUCT_REVIEWS_PER_PAGE != 0 ? 1 : 0);

        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
        if (page < 1 || page > totalPages) {
            page = 1;
        }

        int offset = (page - 1) * PRODUCT_REVIEWS_PER_PAGE;

        List<ProductReview> productReviews = Protector.of(() -> productReviewDAO.getOrderedPart(
                PRODUCT_REVIEWS_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        for (ProductReview productReview : productReviews) {
            Protector.of(() -> userDAO.selectPrevalue(productReview.getUserId())).get()
                    .ifPresent(productReview::setUser);
            Protector.of(() -> productDAO.selectPrevalue(productReview.getProductId())).get()
                    .ifPresent(productReview::setProduct);
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("productReviews", productReviews);
        request.getRequestDispatcher("/WEB-INF/views/productReviewManagerView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
