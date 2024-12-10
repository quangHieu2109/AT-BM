package com.bookshopweb.mapper;

import com.bookshopweb.beans.OrderItem;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem map(ResultSet rs, StatementContext ctx) throws SQLException {
        OrderItem result = new OrderItem();
        result.setId(rs.getLong("id"));
        result.setOrderId(rs.getLong("orderId"));
        result.setProductId(rs.getLong("productId"));
        result.setPrice(rs.getDouble("price"));
        result.setDiscount(rs.getDouble("discount"));
        result.setQuantity(rs.getInt("quantity"));
        result.setCreatedAt(rs.getTimestamp("createdAt"));
        result.setUpdatedAt(rs.getTimestamp("updatedAt"));


        return result;
    }
}
