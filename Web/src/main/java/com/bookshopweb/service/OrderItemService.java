//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.OrderItem;
//import com.bookshopweb.dao.OrderItemIDAO;
//
//import java.util.List;
//
//public class OrderItemService extends Service<OrderItem, OrderItemIDAO> implements OrderItemIDAO {
//    public OrderItemService() {
//        super(OrderItemIDAO.class);
//    }
//
//    @Override
//    public void bulkInsert(List<OrderItem> orderItems) {
//        jdbi.useExtension(OrderItemIDAO.class, dao -> dao.bulkInsert(orderItems));
//    }
//
//    @Override
//    public List<String> getProductNamesByOrderId(long orderId) {
//        return jdbi.withExtension(OrderItemIDAO.class, dao -> dao.getProductNamesByOrderId(orderId));
//    }
//
//    @Override
//    public List<OrderItem> getByOrderId(long orderId) {
//        return jdbi.withExtension(OrderItemIDAO.class, dao -> dao.getByOrderId(orderId));
//    }
//}
