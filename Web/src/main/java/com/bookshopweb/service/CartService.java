//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.Cart;
//import com.bookshopweb.dao.CartIDAO;
//
//import java.util.Optional;
//
//public class CartService extends Service<Cart, CartIDAO> implements CartIDAO {
//    public CartService() {
//        super(CartIDAO.class);
//    }
//
//    @Override
//    public Optional<Cart> getByUserId(long userId) {
//        return jdbi.withExtension(CartIDAO.class, dao -> dao.getByUserId(userId));
//    }
//
//    @Override
//    public int countCartItemQuantityByUserId(long userId) {
//        return jdbi.withExtension(CartIDAO.class, dao -> dao.countCartItemQuantityByUserId(userId));
//    }
//
//    @Override
//    public int countOrderByUserId(long userId) {
//        return jdbi.withExtension(CartIDAO.class, dao -> dao.countOrderByUserId(userId));
//    }
//
//    @Override
//    public int countOrderDeliverByUserId(long userId) {
//        return jdbi.withExtension(CartIDAO.class, dao -> dao.countOrderDeliverByUserId(userId));
//    }
//
//    @Override
//    public int countOrderReceivedByUserId(long userId) {
//        return jdbi.withExtension(CartIDAO.class, dao -> dao.countOrderReceivedByUserId(userId));
//    }
//}
