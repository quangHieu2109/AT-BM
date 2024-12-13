package com.bookshopweb.dao;

import com.bookshopweb.beans.OTP;
import com.bookshopweb.jdbiInterface.OtpJDBI;
import com.bookshopweb.utils.JDBIUltis;

import java.util.List;

public class OTPDAO {
    private OtpJDBI otpJDBI = JDBIUltis.getJDBI().onDemand(OtpJDBI.class);
    public List<OTP> getAll(){
        return otpJDBI.getAll();
    }
    public OTP getByUserId(long userId){
        return otpJDBI.getByUserId(userId);
    }
    public OTP getById(long id){
        return otpJDBI.getById(id);
    }
    public int removeByUserId(long userId){
        return otpJDBI.removeByUserId(userId);
    }
    public int updateByOTP(OTP otp){
        return otpJDBI.updateByOTP(otp);
    }

    // Trả về id khi insert thành công
    public long addOtpWithoutId(OTP otp){

        return JDBIUltis.getJDBI().withHandle(handle ->
                handle.createUpdate("INSERT INTO otp (userId, otp, expireAt) VALUES (:userId, :otp, :expireAt)")
                        .bindBean(otp)
                        .executeAndReturnGeneratedKeys("id") // Lấy ID
                        .mapTo(Long.class) // Chuyển đổi thành Long
                        .findOne()
                        .orElse(0l)
        );
    }
    public int addOtpFullInfo(OTP otp){
        return otpJDBI.addOtpFullInfo(otp);
    }

}
