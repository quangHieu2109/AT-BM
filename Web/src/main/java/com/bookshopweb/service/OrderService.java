//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.Order;
//import com.bookshopweb.dao.OrderIDAO;
//
//import java.util.List;
//
//public class OrderService extends Service<Order, OrderIDAO> implements OrderIDAO {
//    public OrderService() {
//        super(OrderIDAO.class);
//    }
//
//    @Override
//    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
//        return jdbi.withExtension(OrderIDAO.class, dao -> dao.getOrderedPartByUserId(userId, limit, offset));
//    }
//
//    @Override
//    public int countByUserId(long userId) {
//        return jdbi.withExtension(OrderIDAO.class, dao -> dao.countByUserId(userId));
//    }
//
//    @Override
//    public void cancelOrder(long id) {
//        jdbi.useExtension(OrderIDAO.class, dao -> dao.cancelOrder(id));
//    }
//
//    @Override
//    public int count() {
//        return jdbi.withExtension(OrderIDAO.class, OrderIDAO::count);
//    }
//
//    @Override
//    public void confirm(long id) {
//        jdbi.useExtension(OrderIDAO.class, dao -> dao.confirm(id));
//    }
//
//    @Override
//    public void cancel(long id) {
//        jdbi.useExtension(OrderIDAO.class, dao -> dao.cancel(id));
//    }
//
//    @Override
//    public void reset(long id) {
//        jdbi.useExtension(OrderIDAO.class, dao -> dao.reset(id));
//    }
//}
