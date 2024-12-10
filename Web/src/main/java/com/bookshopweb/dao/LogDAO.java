package com.bookshopweb.dao;

import com.bookshopweb.beans.Log;
import com.bookshopweb.jdbiInterface.LogJDBI;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {
    Connection conn = JDBCUtils.getConnection();
    private static LogJDBI logJDBI= JDBIUltis.getJDBI().onDemand(LogJDBI.class);
    public List<Log> selectAll(){
        List<Log> result = logJDBI.selectAll();
//        try {
//            String sql = "select * from log";
//            PreparedStatement st = conn.prepareStatement(sql);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()){
//                long id = rs.getLong("id");
//                String ip = rs.getString("ip");
//                int levelLog = rs.getInt("levelLog");
//                String res = rs.getString("res");
//                String preValue = rs.getString("preValue");
//                String curValue = rs.getString("curValue");
//                Timestamp createAt = rs.getTimestamp("createAt");
//                Timestamp updateAt = rs.getTimestamp("updateAt");
//                result.add(new Log(id, ip, levelLog, res, preValue, curValue, createAt, updateAt));
//            }
//            rs.close();
//            st.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return result;
    }
    public int insert(Log log) {
        int result =logJDBI.insertLog(log);
//        try {
//            String sql = "insert into log(id, ip, levelLog, res, preValue, curValue, createAt, updateAt) " +
//                    "values(?,?,?,?,?,?,?,?)";
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setLong(1, log.getId());
//            st.setString(2, log.getIp());
//            st.setInt(3, log.getLevelLog());
//            st.setString(4, log.getResource());
//            st.setString(5, log.getPreValue());
//            st.setString(6, log.getCurValue());
//            st.setTimestamp(7, log.getCreateAt());
//            st.setTimestamp(8, log.getUpdateAt());
//            result = st.executeUpdate();
//            st.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        return result;
    }

    public int update(Log log) {
        int result =logJDBI.insertLog(log);
//        try {
//            String sql = "update log set levelLog=? where id =?";
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setInt(1, log.getLevelLog());
//            st.setLong(2, log.getId());
//            result = st.executeUpdate();
//            st.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return result;
    }

    public int delete(Log log) {
        int result =logJDBI.deleteLog(log);
//        try {
//            String sql = "delete from log where id =?";
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setLong(1, log.getId());
//            result = st.executeUpdate();
//            st.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return result;
    }

    public static void main(String[] args) {
//        Log log = new Log(1, "4564256", 11, "rs11", "pre", "cur", null, null);
//        LogDAO dao = new LogDAO();
        for(Log l : logJDBI.selectAll()){
            System.out.println(l);
        }
//        Log log = new Log();
//        log.setId(Long.valueOf("171155319043"));
//        log.setLevelLog(1);
//        log.setResource("Hiu");
//        log.setCreateAt(new Timestamp(123212312));
//        System.out.println(logJDBI.insertLog(log));
    }
}