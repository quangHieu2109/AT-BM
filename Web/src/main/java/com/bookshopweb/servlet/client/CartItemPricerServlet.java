package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.Product;
import com.bookshopweb.dao.CartItemDAO;
import com.bookshopweb.dao.ProductDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/cartItemPriceServlet")
@MultipartConfig
public class CartItemPricerServlet extends HttpServlet {
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private ProductDAO productDAO = new ProductDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] cartItemIdsString = req.getParameterValues("cartItemIds");
        List<Long> cartItemIds = new ArrayList<>();
        List<CartItem> cartItems = new ArrayList<>();
        double totalPrice=0 ;
        try {
            for(String s : cartItemIdsString){
                cartItemIds.add(Long.parseLong(s));
            }

        } catch (Exception e) {
            cartItemIds = new ArrayList<>();
            String cartItemId = Arrays.toString(cartItemIdsString);
            String cartItemIdNew="";
            for(int i=1; i<cartItemId.length()-1; i++){
                cartItemIdNew += cartItemId.charAt(i);
            }
            for (String s : cartItemIdNew.split(",")) {
                cartItemIds.add(Long.parseLong(s));

            }
        }
        for(long id: cartItemIds){
            cartItems.add(cartItemDAO.selectPrevalue(id));
        }
        for(CartItem cartItem: cartItems){
            Product product = productDAO.getByIdProduct(cartItem.getProductId());
            totalPrice += cartItem.getQuantity() *(product.getPrice() *(1-product.getDiscount()/100));
        }
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("totalPrice", totalPrice);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());
    }
}
