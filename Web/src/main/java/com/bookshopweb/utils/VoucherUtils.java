package com.bookshopweb.utils;

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.CategorysOfVoucher;
import com.bookshopweb.beans.Product;
import com.bookshopweb.beans.Voucher;
import com.bookshopweb.dao.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoucherUtils {
    private static VoucherDAO voucherDAO = new VoucherDAO();
    private static CategoryDAO categoryDAO = new CategoryDAO();
    private static CategorysOfVoucherDAO categorysOfVoucherDAO = new CategorysOfVoucherDAO();
    private static CartItemDAO cartItemDAO = new CartItemDAO();
    private static ProductDAO productDAO = new ProductDAO();
    public static double getDecrease(long voucherId, String[] cartItemIdsString, double ship){
        List<Long> cartItemIds = new ArrayList<>();
        List<CartItem> cartItems = new ArrayList<>();
        Voucher voucher = voucherDAO.selectPrevalue(voucherId);
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
        List<CategorysOfVoucher> categorysOfVouchers = categorysOfVoucherDAO.getByVoucherId(voucherId);
        double totalPrice = 0;
        //categorysOfVouchers.size() ==0 tức là voucher áp dụng cho mọi thể loại sản phẩm
        if(categorysOfVouchers.size() ==0){
            for(CartItem cartItem: cartItems){
                Product product = productDAO.getByIdProduct(cartItem.getProductId());
                totalPrice += cartItem.getQuantity() *(product.getPrice() *(1-product.getDiscount()/100));
            }
        }else{
            //voucher chỉ áp dụng cho 1 số thể loại nhất định
            //nên phải kiểm tra xem đơn hàng có sản phẩm phù hợp hay không
            List<Long> categoryIds = new ArrayList<>();
            for(CategorysOfVoucher categorysOfVoucher: categorysOfVouchers){
                categoryIds.add(categorysOfVoucher.getCategoryId());
            }
            for(CartItem cartItem: cartItems){
                Product product = productDAO.getByIdProduct(cartItem.getProductId());
                if(categoryIds.contains(productDAO.getCategpryId(product.getId()))){
                    totalPrice += cartItem.getQuantity() *(product.getPrice() *(1-product.getDiscount()/100));
                }
            }

        }
        double decrease =0;
        if(voucher.getType() ==0){
            if(totalPrice >= voucher.getMinPrice()){
                decrease = Math.min(voucher.getMaxDecrease(), ship*voucher.getPercentDecrease()/100);
            }
        }else{
            if(totalPrice >= voucher.getMinPrice()){
                decrease = Math.min(voucher.getMaxDecrease(), totalPrice*voucher.getPercentDecrease()/100);
            }
        }
        return decrease;
    }
    public static List<Long> convertToListLong(String[] cartItemIdsString){
        List<Long> cartItemIds = new ArrayList<>();

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


        return cartItemIds;
    }
}
