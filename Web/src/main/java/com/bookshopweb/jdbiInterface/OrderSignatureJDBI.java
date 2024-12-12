package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.OrderSignature;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(OrderSignature.class)
public interface OrderSignatureJDBI {
    @SqlQuery("SELECT * FROM order_signature")
    List<OrderSignature> getAll();

    @SqlQuery("SELECT * FROM order_signature WHERE id=:id")
    OrderSignature getById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM order_signature WHERE orderId=:orderId")
    OrderSignature getByOrderId(@Bind("orderId") long orderId);

    @SqlUpdate("INSERT INTO order_signature (orderId, signature, createdAt, status)\n" +
            "VALUES (:orderId, :signature, :createdAt, :status)")
    int addOrderSignature(@BindBean OrderSignature orderSignature);

    @SqlUpdate("UPDATE order_signature SET signature =:signature, updatedAt = :updatedAt, status:status " +
            "WHERE id = :id")
    int updateOrderSignature(@BindBean OrderSignature orderSignature);
}
