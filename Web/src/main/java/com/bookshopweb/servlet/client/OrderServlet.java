package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.dto.OrderResponse;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.VoucherUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "OrderServlet", value = "/order")
@MultipartConfig
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();

    private static final int ORDERS_PER_PAGE = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        if (user != null) {
            int totalOrders = orderDAO.countByUserId(user.getId());


            // Tính tổng số trang (= tổng số order / số sản phẩm trên mỗi trang)
            int totalPages = totalOrders / ORDERS_PER_PAGE;
            if (totalOrders % ORDERS_PER_PAGE != 0) {
                totalPages++;
            }

            // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang) thì gán bằng 1
            String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
            int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
            if (page < 1 || page > totalPages) {
                page = 1;
            }
        // Tính mốc truy vấn (offset)
            int offset = (page - 1) * ORDERS_PER_PAGE;

            // Lấy danh sách order, lấy với số lượng là ORDERS_PER_PAGE và tính từ mốc offset
            List<Order> orders = Protector.of(() -> orderDAO.getOrderedPartByUserId(
                    user.getId(), ORDERS_PER_PAGE, offset
            )).get(ArrayList::new);
//
            List<OrderResponse> orderResponses = new ArrayList<>();
//
            for (Order order : orders) {
                List<OrderItem> orderItems = Protector.of(() -> orderItemDAO.getByOrderId(order.getId())).get(ArrayList::new);

                double total = 0.0;

                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getDiscount() == 0) {
                        total += orderItem.getPrice() * orderItem.getQuantity();
                    } else {
                        total += (orderItem.getPrice() * (100 - orderItem.getDiscount()) / 100) * orderItem.getQuantity();
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDateTime = sdf.format(order.getCreatedAt());

//
                OrderResponse orderResponse = new OrderResponse(
                        order.getId(),
                        formattedDateTime,
                        check(orderItemDAO.getProductNamesByOrderId(order.getId())),
                        order.getStatus(),
                        total + order.getDeliveryPrice());

                orderResponses.add(orderResponse);
            }

            request.setAttribute("totalPages", totalPages);
            request.setAttribute("page", page);
            request.setAttribute("orders", orderResponses);
        }

        request.getRequestDispatcher("/WEB-INF/views/orderView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
//        System.out.println("unitshipVal "+ req.getParameter("unitshipVal"));
//        System.out.println("addressId "+ req.getParameter("addressId"));
//        System.out.println("cartItemIds "+ Arrays.toString(req.getParameterValues("cartItemIds")));
//        System.out.println("ship "+ req.getParameter("ship"));
//        System.out.println("unitshipVal "+ req.getParameter("unitshipVal"));
        int deliveryMethod = Integer.parseInt(req.getParameter("unitshipVal"));
        long shipVoucherId = Long.parseLong(req.getParameter("shipVoucherId"));
        long productVoucherId = Long.parseLong(req.getParameter("productVoucherId"));
        long addressId =Long.parseLong(req.getParameter("addressId"));

        String[] cartItemIdsString = req.getParameterValues("cartItemIds");
        List<Long> cartItemIds = VoucherUtils.convertToListLong(cartItemIdsString);
        List<CartItem> cartItems = new ArrayList<>();
        for(long id : cartItemIds){
            cartItems.add(cartItemDAO.selectPrevalue(id));
        }

        double ship = Double.parseDouble(req.getParameter("ship"));
        double shipVoucherDecrease =0, productVoucherDecrease=0;
        if(shipVoucherId>0){
            shipVoucherDecrease = -VoucherUtils.getDecrease(shipVoucherId, cartItemIdsString, ship);
        }
        if(productVoucherId>0){
            productVoucherDecrease = -VoucherUtils.getDecrease(productVoucherId, cartItemIdsString, ship);
        }

        Order order = new Order();
        order.setId(Calendar.getInstance().getTimeInMillis());
        order.setUserId(user.getId());
        order.setStatus(0);
        order.setDeliveryMethod(deliveryMethod);
        order.setDeliveryPrice(ship);
        order.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        double totalPrice =0;
        int rs = orderDAO.insert(order, IPUtils.getIP(req));
        if(rs > 0){
            for(CartItem cartItem : cartItems){
                Product product = productDAO.selectPrevalue(cartItem.getProductId());
                OrderItem orderItem = new OrderItem();
                orderItem.setId(0l);
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setDiscount(product.getDiscount());
                orderItem.setPrice(product.getPrice());
                orderItem.setCreatedAt(order.getCreatedAt());
                orderItemDAO.insert(orderItem, "");
                totalPrice += cartItem.getQuantity() * (1-product.getDiscount()/100)*product.getPrice();
            }
            totalPrice += shipVoucherDecrease;
            totalPrice += productVoucherDecrease;
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());

            orderDetail.setShipVoucherDecrease(shipVoucherDecrease);
            orderDetail.setProductVoucherDecrease(productVoucherDecrease);
            orderDetail.setAddressId(addressId);
            orderDetail.setTotalPrice(totalPrice);
            System.out.println(orderDetail);
            orderDetailDAO.addOrderDetailNoVoucher(orderDetail);
            if(shipVoucherId > 0){
                orderDetailDAO.updateShipVoucherId(shipVoucherId, orderDetail.getOrderId());
                voucherDAO.decreaseQuantity(shipVoucherId);
            }
            if(productVoucherId > 0){
                orderDetailDAO.updateProductVoucherId(productVoucherId, orderDetail.getOrderId());
                voucherDAO.decreaseQuantity(productVoucherId);

            }
            for(CartItem cartItem : cartItems){
                cartItemDAO.delete(cartItem, IPUtils.getIP(req));
            }
        }


    }

    private String check(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        return list.get(0) + " và " + (list.size() - 1) + " sản phẩm khác";
    }
}
