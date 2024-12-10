package com.bookshopweb.dao;

import com.bookshopweb.beans.GoogleUser;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoogleUserDAO {
    private Connection conn = JDBCUtils.getConnection();
    public int deleteByUserId(long userId) {

        int result = 0;
        try{
            String sql = "delete from google_user where userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,userId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public GoogleUser selectByEmail(String email) {
        GoogleUser result = null;
        try {
            String sql = "select * from google_user where email=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("userId");
                result = new GoogleUser(email, id);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public int insert(GoogleUser user){
        int result =0;
        try {
            String sql = "insert into google_user(email, userId) " +
                    "values(?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, user.getEmail());
            st.setLong(2, user.getId());
            result = st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
