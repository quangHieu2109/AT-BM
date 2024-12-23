package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.dto.OrderResponse;
import com.bookshopweb.utils.HashUtils;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.VoucherUtils;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrderSignatureServlet", value = "/orderSignature")
@MultipartConfig
public class OrderSisnatureServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();

    private static final int ORDERS_PER_PAGE = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();
        long orderId = Long.parseLong(request.getParameter("orderId"));
        Order order = orderDAO.selectPrevalue(orderId);
        if(order == null){
            response.setStatus(400);
            response.getWriter().write("OrderId không chính xác");
            return;
        }
        String hashOrder = HashUtils.hash(order.getInfo());
        OrderSignature orderSignature = orderSignatureDAO.getByOrderId(orderId);
        if(orderSignature == null || orderSignature.getHashOrderInfo().equals(hashOrder)){
            jsonObject.addProperty("edited", false);
        }else{
            jsonObject.addProperty("edited", true);
        }
        response.getWriter().write(jsonObject.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
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
        order.setStatus(-1);
        order.setDeliveryMethod(deliveryMethod);
        order.setDeliveryPrice(ship);
        order.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        double totalPrice =0;
//        int rs = orderDAO.insert(order, IPUtils.getIP(req));
        int rs = orderDAO.insert(order, "");



        if(rs > 0){
            order.setId(rs);
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
            orderDetail.setTotalPrice(Math.round(totalPrice));
//            System.out.println(orderDetail);
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
//                cartItemDAO.delete(cartItem, IPUtils.getIP(req));
                cartItemDAO.delete(cartItem, "");
            }
            OrderSignature orderSignature = new OrderSignature(order.getId(), HashUtils.hash(orderDAO.selectPrevalue(order.getId()).getInfo()), new Timestamp(System.currentTimeMillis()), 1);
            orderSignatureDAO.addOrderSignature(orderSignature);
        }


    }

    private String check(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        return list.get(0) + " và " + (list.size() - 1) + " sản phẩm khác";
    }
}
