package com.bookshopweb.mapper;

import com.bookshopweb.beans.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        Product result = new Product();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setPrice(rs.getDouble("price"));
        result.setDiscount(rs.getDouble("discount"));
        result.setQuantity(rs.getInt("quantity"));
        result.setTotalBuy(rs.getInt("totalBuy"));
        result.setAuthor(rs.getString("author"));
        result.setPages(rs.getInt("pages"));
        result.setPublisher(rs.getString("publisher"));
        result.setYearPublishing(rs.getInt("yearPublishing"));
        result.setDescription(rs.getString("description"));
        result.setImageName(rs.getString("imageName"));
        result.setShop(rs.getInt("shop"));
        result.setCreatedAt(rs.getTimestamp("createdAt"));
        result.setUpdatedAt(rs.getTimestamp("updatedAt"));
        result.setStartsAt(rs.getTimestamp("startsAt"));
        result.setStartsAt(rs.getTimestamp("endsAt"));


        return result;
    }
}
