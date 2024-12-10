package com.bookshopweb.servlet.client.productreview;

import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.ProductReviewDAO;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "EditProductReviewServlet", value = "/editProductReview")
public class EditProductReviewServlet extends HttpServlet {
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
            Protector.of(() -> userDAO.selectPrevalue(productReview.getUserId())).get()
                    .ifPresent(productReview::setUser);
            Protector.of(() -> productDAO.selectPrevalue(productReview.getProductId())).get()
                    .ifPresent(productReview::setProduct);
            boolean isExactUser = Optional.ofNullable((User) request.getSession().getAttribute("currentUser"))
                    .map(u -> u.getId() == productReview.getUserId())
                    .orElse(false);
            if (isExactUser) {
                request.setAttribute("productReview", productReview);
                request.getRequestDispatcher("/WEB-INF/views/editProductReviewView.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<ProductReview> productReviewFromServer = Protector.of(() -> productReviewDAO.selectPrevalue(id))
                .get();

        ProductReview productReview = productReviewFromServer.orElseGet(ProductReview::new);
        productReview.setRatingScore(Protector.of(() -> Integer.parseInt(request.getParameter("ratingScore"))).get(0));
        productReview.setContent(request.getParameter("content"));
        Protector.of(() -> userDAO.selectPrevalue(productReview.getUserId())).get()
                .ifPresent(productReview::setUser);
        Protector.of(() -> productDAO.selectPrevalue(productReview.getProductId())).get()
                .ifPresent(productReview::setProduct);

        Map<String, List<String>> violations = new HashMap<>();
        violations.put("ratingScoreViolations", Validator.of(productReview.getRatingScore())
                .isNotNull()
                .toList());
        violations.put("contentViolations", Validator.of(productReview.getContent())
                .isNotNullAndEmpty()
                .isAtLeastOfLength(10)
                .toList());

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();
        String successMessage = "Đã sửa đánh giá thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        if (sumOfViolations == 0) {
            Protector.of(() -> productReviewDAO.update(productReview, IPUtils.getIP(request)))
                    .done(r -> request.setAttribute("successMessage", successMessage))
                    .fail(e -> request.setAttribute("errorMessage", errorMessage));
        } else {
            request.setAttribute("violations", violations);
        }

        request.setAttribute("productReview", productReview);
        request.getRequestDispatcher("/WEB-INF/views/editProductReviewView.jsp").forward(request, response);
    }
}
