package com.bookshopweb.dao;

import com.bookshopweb.beans.User;
import com.bookshopweb.utils.JDBIUltis;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jdbi.v3.core.Handle;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws SQLException {
//        UserJDBI userJDBI = JDBIUltis.getJDBI().onDemand(UserJDBI.class);
//        List<User> users = userJDBI.selectAll(); // lấy dữ liệu thông qua map constructor
//        List<User> users = JDBIUltis.getJDBI().withHandle( handle ->
//                handle.createQuery("select * from user").map(new UserMapper())
//                        .list()); // lấy dữ liệu thông qua mapper object
        System.out.println(new OrderItemDAO().getTotalPriceByOrderId(1));


    }
}
