package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.OrderItem;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(OrderItem.class)
public interface OrderItemJDBI {
    @SqlQuery("select sum(price) from order_item where orderId=:orderId")
    int getTotalPriceByOrderId(@Bind("orderId") long orderId);
    @SqlQuery("select count(*) from order_item where orderId=:orderId")
    int getQuantityByOrderId(@Bind("orderId") long orderId);
}
