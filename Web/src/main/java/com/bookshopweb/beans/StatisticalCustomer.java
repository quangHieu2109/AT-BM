package com.bookshopweb.beans;

import java.sql.Timestamp;

public class StatisticalCustomer {
    private long userId;
    private String userName;
    private long lasOrderId;
    private Timestamp lastOrderTime;

    public StatisticalCustomer(long userId, String userName, long lasOrderId, Timestamp lastOrderTime) {
        this.userId = userId;
        this.userName = userName;
        this.lasOrderId = lasOrderId;
        this.lastOrderTime = lastOrderTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLasOrderId() {
        return lasOrderId;
    }

    public void setLasOrderId(long lasOrderId) {
        this.lasOrderId = lasOrderId;
    }

    public Timestamp getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(Timestamp lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    @Override
    public String toString() {
        return "StatisticalCustomer{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", lasOrderId=" + lasOrderId +
                ", lastOrderTime=" + lastOrderTime +
                '}';
    }
}
