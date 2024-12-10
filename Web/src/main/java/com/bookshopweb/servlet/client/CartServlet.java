package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.beans.User;
import com.bookshopweb.dto.ErrorMessage;
import com.bookshopweb.dto.OrderRequest;
import com.bookshopweb.dto.SuccessMessage;
import com.bookshopweb.dao.CartDAO;
import com.bookshopweb.dao.OrderItemDAO;
import com.bookshopweb.dao.OrderDAO;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.JsonUtils;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy đối tượng orderRequest từ JSON trong request
        OrderRequest orderRequest = JsonUtils.get(request, OrderRequest.class);

        // Tạo order
        Order order = new Order(
                0L,
                orderRequest.getUserId(),
                1,
                orderRequest.getDeliveryMethod(),
                orderRequest.getDeliveryPrice(),
                Timestamp.from(Instant.now()),
                null
        );
        orderDAO.insert(order, IPUtils.getIP(request));

        List<Order> o  = orderDAO.getOrderedPartByUserId(orderRequest.getUserId(),2,0);
        long orderId =  o.get(0).getId();
//        System.out.println(o.size());
//        System.out.println("Order ID: " + orderId);
        String successMessage = "Đã đặt hàng và tạo đơn hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new SuccessMessage(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new ErrorMessage(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (orderId > 0) {

            List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(orderItemRequest -> new OrderItem(
                    0L,
                    orderId,
                    orderItemRequest.getProductId(),
                    orderItemRequest.getPrice(),
                    orderItemRequest.getDiscount(),
                    orderItemRequest.getQuantity(),
                    Timestamp.from(Instant.now()),
                    null
            )).collect(Collectors.toList());

            Protector.of(() -> {
                        orderItemDAO.bulkInsert(orderItems);
                        cartDAO.delete(cartDAO.selectPrevalue(orderRequest.getCartId()),IPUtils.getIP(request));
                    })
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
        } else {
            failFunction.run();
        }
    }
}
