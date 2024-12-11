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
    public int addOrderSignature(OrderSignature orderSignature){
        return orderSignatureJDBI.addOrderSignature(orderSignature);
    }
    public int updateOrderSignature(OrderSignature orderSignature){
        return orderSignatureJDBI.updateOrderSignature(orderSignature);
    }
}
