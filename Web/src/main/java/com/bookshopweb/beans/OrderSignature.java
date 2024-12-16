package com.bookshopweb.beans;

import java.sql.Timestamp;

public class OrderSignature {
    private long id;
    private long orderId;
    private long authId;
    private String signature;
    private String hashOrderInfo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int status;

    public OrderSignature(long id, long orderId, long authId, String signature, String hashOrderInfo, Timestamp createdAt, Timestamp updatedAt, int status) {
        this.id = id;
        this.orderId = orderId;
        this.authId = authId;
        this.signature = signature;
        this.hashOrderInfo = hashOrderInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public OrderSignature(long orderId, String hashOrderInfo, Timestamp createdAt, int status) {
        this.orderId = orderId;
        this.hashOrderInfo = hashOrderInfo;
        this.createdAt = createdAt;
        this.status = status;
    }

    public OrderSignature(long orderId, long authId, String signature, String hashOrderInfo, Timestamp createdAt, Timestamp updatedAt, int status) {
        this.orderId = orderId;
        this.authId = authId;
        this.signature = signature;
        this.hashOrderInfo = hashOrderInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public OrderSignature() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }

    public String getHashOrderInfo() {
        return hashOrderInfo;
    }

    public void setHashOrderInfo(String hashOrderInfo) {
        this.hashOrderInfo = hashOrderInfo;
    }

    @Override
    public String toString() {
        return "OrderSignature{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", signature='" + signature + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}
