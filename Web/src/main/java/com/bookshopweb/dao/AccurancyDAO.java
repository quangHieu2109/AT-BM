package com.bookshopweb.dao;

import com.bookshopweb.beans.AccurancyUser;

import com.bookshopweb.beans.User;
import com.bookshopweb.jdbiInterface.UserJDBI;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class AccurancyDAO {
    private Connection conn = JDBCUtils.getConnection();

    public int deleteByUserId(long userId) {
        User user = JDBIUltis.getJDBI().onDemand(UserJDBI.class).getById(userId);
        int result = 0;
        try{
            String sql = "delete from accuracyuser where username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public AccurancyUser getByUserName(String userName) {
        AccurancyUser result = null;
        try {
            String sql = "select * from accuracyuser where username=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String accurancyCode = rs.getString("accuracyCode");
                Timestamp endAt = rs.getTimestamp("endAt");
                result = new AccurancyUser(id, userName, accurancyCode, endAt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int insertAccurancy(AccurancyUser accurancyUser) {
        int result = 0;
        try {
           if(getByUserName(accurancyUser.getUserName()) ==null){
               String sql = "insert into accuracyuser (username, accuracyCode, endAt) " +
                       "values(?,?,?)";
               PreparedStatement st = conn.prepareStatement(sql);
               st.setString(1, accurancyUser.getUserName());
               st.setString(2, accurancyUser.getAccurancyCode());
               st.setTimestamp(3, accurancyUser.getEndAt());
               result = st.executeUpdate();
           }else{
               result = updateAccurancy(accurancyUser);
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int updateAccurancy(AccurancyUser accurancyUser) {
        int result = 0;
        try {
            String sql = "update accuracyuser set accuracyCode=?, endAt=?" +
                    " where username=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, accurancyUser.getAccurancyCode());
            st.setTimestamp(2, accurancyUser.getEndAt());
            st.setString(3, accurancyUser.getUserName());
            result = st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    public int delete(String userName){
        int result =0;
        try {
            String sql = "delete from accuracyuser where username=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, userName);
            result = st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
