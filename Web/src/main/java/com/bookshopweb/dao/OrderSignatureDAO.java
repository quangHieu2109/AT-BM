package com.bookshopweb.dao;

import com.bookshopweb.beans.OrderSignature;
import com.bookshopweb.jdbiInterface.OrderSignatureJDBI;
import com.bookshopweb.utils.JDBIUltis;

import java.util.List;

public class OrderSignatureDAO {
    private OrderSignatureJDBI orderSignatureJDBI = JDBIUltis.getJDBI().onDemand(OrderSignatureJDBI.class);
    public List<OrderSignature> getAll(){
        return orderSignatureJDBI.getAll();
    }
    public OrderSignature getById(long id){
        return orderSignatureJDBI.getById(id);
    }
    public OrderSignature getByOrderId(long orderId){
        return orderSignatureJDBI.getByOrderId(orderId);
    }
    // Trả về id khi insert thành công
    public long addOrderSignature(OrderSignature orderSignature){
        return JDBIUltis.getJDBI().withHandle(handle ->
                handle.createUpdate("INSERT INTO order_signature (orderId, signature, createdAt, status)\n" +
                                "VALUES (:orderId, :signature, :createdAt, :status)")
                        .bindBean(orderSignature)
                        .executeAndReturnGeneratedKeys("id") // Lấy ID
                        .mapTo(Long.class) // Chuyển đổi thành Long
                        .findOne()
                        .orElse(0l)
        );
    }
    public int updateOrderSignature(OrderSignature orderSignature){
        return orderSignatureJDBI.updateOrderSignature(orderSignature);
    }
}
