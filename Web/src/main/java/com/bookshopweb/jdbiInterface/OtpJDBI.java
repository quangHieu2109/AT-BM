package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.OTP;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(OTP.class)
public interface OtpJDBI {
    @SqlQuery("SELECT * FROM otp")
    List<OTP> getAll();

    @SqlQuery("SELECT * FROM otp WHERE id = :id")
    OTP getById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM otp WHERE userId = :userId")
    OTP getByUserId(@Bind("userId") long userId);

    @SqlUpdate("DELETE from otp WHERE userId = :userId")
    int removeByUserId(@Bind("userId") long userId);

    @SqlUpdate("UPDATE otp SET otp=:otp, expireAt=:expireAt, status=:status WHERE id = :id")
    int updateByOTP(@BindBean OTP otp);

    // cho id trong sql tự tăng
    @SqlUpdate("INSERT INTO otp (userId, otp, expireAt)\n" +
            "VALUES (:userId, :otp, :expireAt)")
    int addOtpWithoutId(@BindBean OTP otp);

    @SqlUpdate("INSERT INTO otp (id, userId, otp, expireAt)\n" +
            "VALUES (:id, :userId, :otp, :expireAt)")
    int addOtpFullInfo(@BindBean OTP otp);

}
