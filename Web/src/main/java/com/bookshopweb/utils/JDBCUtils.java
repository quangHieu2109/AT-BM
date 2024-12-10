package com.bookshopweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {
    public static final String username = "root";
    public static final String password = "";
    private static Connection conn;
    public static void connection(){
        if(conn == null){
            try {
                // Đăng ký MySQL Driver với DriverManager

                DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                // Các thông số
                String url = "jdbc:mySQL://localhost:3306/bookshopdb";


                // Tạo kết nối
                conn = DriverManager.getConnection(url, username, password);
                System.out.println("Kết nối thành công");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Kết nối thất bại");
                e.printStackTrace();
            }

        }

    }
    public static  Connection getConnection(){
        connection();
        return conn;
    }
    public static void closeConnection(){
        try {
            if (conn != null){
                conn.close();
            }
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        connection();
    }
}