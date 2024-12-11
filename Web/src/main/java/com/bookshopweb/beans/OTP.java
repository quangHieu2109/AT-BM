package com.bookshopweb.beans;

import java.sql.Timestamp;

public class OTP {
    private long id;
    private long userId;
    private String otp;
    private Timestamp expireAt;

    public OTP(long id, long userId, String otp, Timestamp expireAt) {
        this.id = id;
        this.userId = userId;
        this.otp = otp;
        this.expireAt = expireAt;
    }
    // không tạo id để lưu vào sql tự tăng
    public OTP(long userId, String otp) {
        this.userId = userId;
        this.otp = otp;
        this.expireAt = new Timestamp(System.currentTimeMillis()+5*60*1000);
    }

    public OTP() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Timestamp expireAt) {
        this.expireAt = expireAt;
    }

    @Override
    public String toString() {
        return "OTP{" +
                "id=" + id +
                ", userId=" + userId +
                ", otp='" + otp + '\'' +
                ", expireAt=" + expireAt +
                '}';
    }
}
