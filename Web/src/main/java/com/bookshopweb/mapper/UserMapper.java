package com.bookshopweb.mapper;

import com.bookshopweb.beans.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User result = new User();
        result.setId(rs.getLong("id"));
        result.setUsername(rs.getString("username"));
        result.setPassword(rs.getString("password"));
        result.setFullname(rs.getString("fullname"));
        result.setEmail(rs.getString("email"));
        result.setPhoneNumber(rs.getString("phoneNumber"));
        result.setGender(rs.getInt("gender"));
        result.setRole(rs.getString("role"));
        result.setCreateAt(rs.getTimestamp("createAt"));
        return result;
    }
}
