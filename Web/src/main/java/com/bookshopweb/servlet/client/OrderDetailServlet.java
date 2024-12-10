package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.Protector;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bookshopweb.utils.DateHelper.formatTimestamp;

@WebServlet(name = "OrderDetailServlet", value = "/orderDetail")
public class OrderDetailServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy id của order và đối tượng order từ database theo id này
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<Order> orderFromServer = Protector.of(() -> orderDAO.getById(id)).get(Optional::empty);

        if (orderFromServer.isPresent()) {
            Order order = orderFromServer.get();
            List<OrderItem> orderItems = Protector.of(() -> orderItemDAO.getByOrderId(id)).get(ArrayList::new);

            double tempPrice = 0;

            for (OrderItem orderItem : orderItems) {
                if (orderItem.getDiscount() == 0) {
                    tempPrice += orderItem.getPrice() * orderItem.getQuantity();
                } else {
                    tempPrice += (orderItem.getPrice() * (100 - orderItem.getDiscount()) / 100) * orderItem.getQuantity();
                }
                orderItem.setProduct(productDAO.getById(orderItem.getProductId()).orElseGet(Product::new));
            }

            request.setAttribute("order", order);
            Timestamp createdAt = order.getCreatedAt(); // Đây là Timestamp của trường createdAt
            String formattedCreatedAt = formatTimestamp(createdAt);
            request.setAttribute("createdAt", formattedCreatedAt);
            request.setAttribute("tempPrice", tempPrice);
            request.setAttribute("orderItems", orderItems);
            request.getRequestDispatcher("/WEB-INF/views/orderDetailView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        String type = request.getParameter("type");
        if (type != null && type.equals("buyagain")) {
            CartDAO cartDAO = new CartDAO();
            CartItemDAO cartItemDAO = new CartItemDAO();
            Cart cart = cartDAO.selectByUser((User) request.getSession().getAttribute("currentUser"));
//            List<Optional<CartItem>> cartItemsFromServer = new ArrayList<>();
            List<OrderItem> orderItems = orderItemDAO.getByOrderId(id);
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            for (OrderItem orderItem : orderItems) {
                long productId = orderItem.getProductId();
                int quantity = orderItem.getQuantity();

                Optional<CartItem> cartItemFromServer = Protector.of(() -> cartItemDAO.getByCartIdAndProductId(cart.getId(), productId)).get(Optional::empty);
                if (cartItemFromServer.isPresent()) {
                    CartItem cartItem = cartItemFromServer.get();
                    jsonArray.add(cartItem.getId());
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    cartItem.setUpdatedAt(Timestamp.from(Instant.now()));
                    cartItemDAO.update(cartItem, IPUtils.getIP(request));

                } else {
                    CartItem cartItem = new CartItem(
                            0L,
                            cart.getId(),
                            productId,
                            quantity,
                            Timestamp.from(Instant.now()),
                            null
                    );
                    cartItemDAO.insert(cartItem, IPUtils.getIP(request));
                    cartItemDAO.getByCartIdAndProductId(cart.getId(), productId).ifPresent(item -> jsonArray.add(item.getId()));

                }
            }
            jsonObject.add("data", jsonArray);

            request.setAttribute("idCheck", jsonObject.toString());
            System.out.println(jsonObject.toString());
            request.setCharacterEncoding("UTF-8");

            request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);

        }
        else {
            Protector.of(() -> orderDAO.cancelOrder(id));
            response.sendRedirect(request.getContextPath() + "/orderDetail?id=" + id);
        }

    }
}
