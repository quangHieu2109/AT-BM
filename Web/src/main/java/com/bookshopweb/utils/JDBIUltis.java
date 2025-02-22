package com.bookshopweb.utils;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class JDBIUltis {
    private static Jdbi jdbi;
    public static Jdbi getJDBI(){
        try {
            if(jdbi == null){
                jdbi = Jdbi.create("jdbc:mysql://localhost:3306/bookshopdb", JDBCUtils.username, JDBCUtils.password);
                jdbi.installPlugin(new SqlObjectPlugin()); // Cần thiết để sử dụng @SqlQuery và @SqlUpdate
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jdbi;
    }

    public static Jdbi getJDBI2(){
        Jdbi reuslt = null;
        try {
            if(reuslt == null){
                reuslt = Jdbi.create("jdbc:mysql://localhost:3306/bookshopdb2", JDBCUtils.username, JDBCUtils.password);
                reuslt.installPlugin(new SqlObjectPlugin()); // Cần thiết để sử dụng @SqlQuery và @SqlUpdate
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return reuslt;
    }
}
