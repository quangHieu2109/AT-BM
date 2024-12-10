//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.CartItem;
//import com.bookshopweb.dao.CartItemIDAO;
//
//import java.util.List;
//import java.util.Optional;
//
//public class CartItemService extends Service<CartItem, CartItemIDAO> implements CartItemIDAO {
//    public CartItemService() {
//        super(CartItemIDAO.class);
//    }
//
//    @Override
//    public List<CartItem> getByCartId(long cartId) {
//        return jdbi.withExtension(CartItemIDAO.class, dao -> dao.getByCartId(cartId));
//    }
//
//    @Override
//    public Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) {
//        return jdbi.withExtension(CartItemIDAO.class, dao -> dao.getByCartIdAndProductId(cartId, productId));
//    }
//
//    @Override
//    public int sumQuantityByUserId(long userId) {
//        return jdbi.withExtension(CartItemIDAO.class, dao -> dao.sumQuantityByUserId(userId));
//    }
//}
