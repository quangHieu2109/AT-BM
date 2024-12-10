package com.bookshopweb.beans;

import java.sql.Timestamp;
import java.util.Calendar;

public class ImportProduct extends AbsModel<ImportProduct>{
    private long id;
    private long productId;
    private long userId;
    private Timestamp importAt;
    private int quantity;
    private double price;
    private Timestamp createAt;

    public ImportProduct() {
    }

    public ImportProduct(long id, long productId, long userId, Timestamp importAt, int quantity, double price) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.importAt = importAt;
        this.quantity = quantity;
        this.price = price;
        this.createAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    public ImportProduct(long id, long productId, long userId, Timestamp importAt, int quantity, double price, Timestamp createAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.importAt = importAt;
        this.quantity = quantity;
        this.price = price;
        this.createAt = createAt;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getImportAt() {
        return importAt;
    }

    public void setImportAt(Timestamp importAt) {
        this.importAt = importAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getResource() {
        return "product_import";
    }

    @Override
    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "ImportProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", importAt=" + importAt +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createAt=" + createAt +
                '}';
    }
}
