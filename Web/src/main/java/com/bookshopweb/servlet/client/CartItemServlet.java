package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Cart;
import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.*;
import com.bookshopweb.dto.CartItemRequest;
import com.bookshopweb.dto.CartItemResponse;
import com.bookshopweb.dto.CartResponse;
import com.bookshopweb.dto.ErrorMessage;
import com.bookshopweb.dto.SuccessMessage;

import com.bookshopweb.dao.CartItemDAO;
import com.bookshopweb.dao.CartDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.IPUtils;

import com.bookshopweb.utils.JsonUtils;
import com.bookshopweb.utils.Protector;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "CartItemServlet", value = "/cartItem")
@MultipartConfig
public class CartItemServlet extends HttpServlet {
    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");
        String type = request.getParameter("type");
        if(type == null){
        // Lấy userId và đối tượng user từ database theo userId này
        long userId = user.getId();
        Optional<User> userFromServer = Protector.of(() -> userDAO.selectPrevalue(userId)).get();


        // Nếu userId là số nguyên dương và có hiện diện trong bảng user
        if (userId > 0 && userFromServer.isPresent()) {
            // Lấy đối tượng cart từ database theo userId
            Optional<Cart> cartFromServer = Protector.of(() -> cartDAO.getByUserId(userId)).get(Optional::empty);

            // Nếu cart của user này đã có trong database

            if (cartFromServer.isPresent()) {
                long cartId = cartFromServer.get().getId();
                List<CartItem> cartItems = Protector.of(() -> cartItemDAO.getByCartId(cartId)).get(ArrayList::new);
                List<CartItemResponse> cartItemResponses = cartItems.stream().map(cartItem -> new CartItemResponse(
                        cartItem.getId(),
                        cartItem.getCartId(),
                        cartItem.getProductId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getDiscount(),
                        cartItem.getProduct().getQuantity(),
                        cartItem.getProduct().getImageName(),
                        cartItem.getQuantity(),
                        categoryDAO.getByProductId(cartItem.getProductId()).get().getName()
                )).collect(Collectors.toList());

                CartResponse cartResponse = new CartResponse(cartId, userId, cartItemResponses);
                JsonUtils.out(response, cartResponse, HttpServletResponse.SC_OK);
            } else {
                CartDAO cartDAO = new CartDAO();
                Cart cart = new Cart(0L, userId, Timestamp.from(Instant.now()), null);
                cartDAO.insert(cart, IPUtils.getIP(request));
                CartResponse cartResponse = new CartResponse(0L, userId, Collections.emptyList());
                JsonUtils.out(response, cartResponse, HttpServletResponse.SC_OK);
            }
        } else {
            String errorMessage = "Đã có lỗi truy vấn!";
            JsonUtils.out(response, new ErrorMessage(404, errorMessage), HttpServletResponse.SC_NOT_FOUND);
        }
        }else{
            int totalQuantity = cartItemDAO.sumQuantityByUserId(user.getId());
            JsonObject jsonResponse = new JsonObject();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            jsonResponse.addProperty("totalQuantity", totalQuantity);
            response.getWriter().write(jsonResponse.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userId = ((User) request.getSession().getAttribute("currentUser")).getId();
        long productId = Long.parseLong(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        // Lấy đối tượng cartItemRequest từ JSON trong request
//        CartItemRequest cartItemRequest = JsonUtils.get(request, CartItemRequest.class);

        // Lấy đối tượng cart từ database theo userId từ cartItemRequest
        Optional<Cart> cartFromServer = Protector.of(() -> cartDAO.getByUserId(userId))
                .get(Optional::empty);

        // Nhận cartId từ cartFromServer (nếu đã có) hoặc cart mới (nếu chưa có)
        long cartId;

        if (cartFromServer.isPresent()) {
            cartId = cartFromServer.get().getId();
        } else {

            Cart cart = new Cart(0L, userId, Timestamp.from(Instant.now()), null);
     

          
            cartId = Protector.of(() -> cartDAO.insert(cart,IPUtils.getIP(request))).get(0);
        }

        String successMessage = "Đã thêm sản phẩm vào giỏ hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new SuccessMessage(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new ErrorMessage(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        // Nếu cart của user này đã có trong database (cardId lớn hơn O)
        if (cartId > 0L) {
            // Lấy đối tượng cartItem từ database theo cartId và productId của cartItemRequest
            Optional<CartItem> cartItemFromServer = Protector.of(() -> cartItemDAO.getByCartIdAndProductId(
                    cartId, productId
            )).get(Optional::empty);

            // Nếu cartItem của cartId và productId này đã có trong database
            if (cartItemFromServer.isPresent()) {
                CartItem cartItem = cartItemFromServer.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setUpdatedAt(Timestamp.from(Instant.now()));
                Protector.of(() -> cartItemDAO.update(cartItem,IPUtils.getIP(request)))
                        .done(r -> doneFunction.run())
                        .fail(e -> failFunction.run());
            } else {
                CartItem cartItem = new CartItem(
                        0L,
                        cartId,
                        productId,
                        quantity,
                        Timestamp.from(Instant.now()),
                        null
                );
                Protector.of(() -> cartItemDAO.insert(cartItem,IPUtils.getIP(request)))
                        .done(r -> doneFunction.run())
                        .fail(e -> failFunction.run());
            }
        } else {
            failFunction.run();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        System.out.println(quantity);

        String successMessage = "Đã cập nhật số lượng của sản phẩm thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";


        CartItem cartItem = cartItemDAO.selectPrevalue(cartItemId);
        cartItem.setQuantity(quantity);
        System.out.println(cartItem);
        int rs = cartItemDAO.update(cartItem,IPUtils.getIP(request));
        int productQuantity = productDAO.selectQuantity(cartItem.getProductId());
        JsonObject jsonResponse = new JsonObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(rs>0){
            response.setStatus(200);
            jsonResponse.addProperty("msg", successMessage);
            jsonResponse.addProperty("productQuantity", productQuantity);
        }else{
            response.setStatus(400);
            jsonResponse.addProperty("msg", errorMessage);

        }
        response.getWriter().write(jsonResponse.toString());

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long cartItemId = Protector.of(() -> Long.parseLong(request.getParameter("cartItemId"))).get(0L);

        String successMessage = "Đã xóa sản phẩm khỏi giỏ hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new SuccessMessage(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new ErrorMessage(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (cartItemId > 0L) {
            Protector.of(() -> cartItemDAO.delete(cartItemDAO.selectPrevalue(cartItemId), IPUtils.getIP(request)))
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
        } else {
            failFunction.run();
        }
    }
}
