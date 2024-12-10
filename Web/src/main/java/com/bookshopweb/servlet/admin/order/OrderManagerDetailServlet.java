package com.bookshopweb.servlet.admin.order;

import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.dao.OrderItemDAO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bookshopweb.servlet.admin.order.OrderManagerServlet.calculateTotalPrice;

@WebServlet(name = "OrderManagerDetailServlet", value = "/admin/orderManager/detail")
public class OrderManagerDetailServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<Order> orderFromServer = Protector.of(() -> orderDAO.selectPrevalue(id)).get();

        if (orderFromServer.isPresent()) {
            Order order = orderFromServer.get();

            Protector.of(() -> userDAO.selectPrevalue(order.getUserId())).get().ifPresent(order::setUser);
            List<OrderItem> orderItems = Protector.of(() -> orderItemDAO.getByOrderId(order.getId())).get(ArrayList::new);
            orderItems.forEach(orderItem -> Protector.of(() -> productDAO.selectById(orderItem.getProductId()))
                    .get()
                    .ifPresent(orderItem::setProduct));
            order.setOrderItems(orderItems);
            order.setTotalPrice(calculateTotalPrice(orderItems, order.getDeliveryPrice()));

            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/orderManagerDetailView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orderManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
