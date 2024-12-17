package com.bookshopweb.dao;

import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderSignature;
import com.bookshopweb.jdbiInterface.OrderSignatureJDBI;
import com.bookshopweb.utils.HashUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.Timestamp;
import java.util.List;

public class OrderSignatureDAO {
    private OrderSignatureJDBI orderSignatureJDBI = JDBIUltis.getJDBI().onDemand(OrderSignatureJDBI.class);

    public List<OrderSignature> getAll() {
        return orderSignatureJDBI.getAll();
    }

    public OrderSignature getById(long id) {
        return orderSignatureJDBI.getById(id);
    }

    public OrderSignature getByOrderId(long orderId) {
        return orderSignatureJDBI.getByOrderId(orderId);
    }

    // Trả về id khi insert thành công
    public long addOrderSignature(OrderSignature orderSignature) {
        OrderSignature signature = getByOrderId(orderSignature.getOrderId());
        if (signature != null) {
            signature.setSignature(orderSignature.getSignature());
            signature.setUpdatedAt(orderSignature.getCreatedAt());
            signature.setStatus(orderSignature.getStatus());
            signature.setAuthId(orderSignature.getAuthId());
            signature.setHashOrderInfo(orderSignature.getHashOrderInfo());

            return updateOrderSignature(signature);
        } else {
            return JDBIUltis.getJDBI().withHandle(handle ->
                    handle.createUpdate("INSERT INTO order_signature (orderId, signature, hashOrderInfo, createdAt, status)\n" +
                                    "VALUES (:orderId, :signature, :hashOrderInfo, :createdAt, :status)")
                            .bindBean(orderSignature)
                            .executeAndReturnGeneratedKeys("id") // Lấy ID
                            .mapTo(Long.class) // Chuyển đổi thành Long
                            .findOne()
                            .orElse(0l)
            );
        }
    }

    public int updateOrderSignature(OrderSignature orderSignature) {
        return orderSignatureJDBI.updateOrderSignature(orderSignature);
    }

    public static void main(String[] args) {
        OrderSignatureJDBI orderSignatureJDBI = JDBIUltis.getJDBI().onDemand(OrderSignatureJDBI.class);
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO()   ;
        OrderDAO orderDAO = new OrderDAO();
        List<OrderSignature> signatures = orderSignatureDAO.getAll();
        for(OrderSignature orderSignature: signatures){
            Order order = orderDAO.selectPrevalue(orderSignature.getOrderId());
            orderSignature.setHashOrderInfo(HashUtils.hash(order.getInfo()));
            orderSignatureJDBI.updateHashOrderInfo(orderSignature);
        }
//        OrderSignature signature = new OrderSignature(2, 1734238184620L, "123456678", new Timestamp(System.currentTimeMillis()), null, 1);
//        System.out.println(orderSignatureDAO.updateOrderSignature(signature));
    }
}
