package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.Order;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Order.class)
public interface OrderJDBI {
    @SqlQuery("select * from orders where status=:status")
    List<Order> getByStatus(@Bind("status") int status);
    @SqlQuery("select * from orders where status=:status limit :start, :length")
    List<Order> getByStatusLimit(@Bind("status") int status,@Bind("start") int start,@Bind("length") int length);
    @SqlUpdate("update orders set status=:status where id=:id")
    int updateStatus(@Bind("status") int status, @Bind("id") long id);
    @SqlQuery("select count(*) from orders where status=:status")
    int getQuantityByStatus(@Bind("status") int status);


}
