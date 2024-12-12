package com.bookshopweb.beans;

import java.sql.Timestamp;

public class Authenticator {
    private long id;
    private long userId;
    private String publicKey;
    private Timestamp createdAt;
    private int status;
    private Timestamp updatedAt;

    public Authenticator(long id, long userId, String publicKey, Timestamp createdAt, int status, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.publicKey = publicKey;
        this.createdAt = createdAt;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Authenticator(long userId, String publicKey, Timestamp createdAt, int status, Timestamp updatedAt) {
        this.userId = userId;
        this.publicKey = publicKey;
        this.createdAt = createdAt;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Authenticator() {
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Authenticator{" +
                "id=" + id +
                ", userId=" + userId +
                ", publicKey='" + publicKey + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}

