package com.bookshopweb.beans;

import com.google.gson.Gson;
import org.jdbi.v3.core.mapper.Nested;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.StringJoiner;

public class CartItem extends AbsModel<CartItem> {
    private long id;
    private long cartId;
    private long productId;
    private int quantity;
    private Timestamp createdAt;
    @Nullable
    private Timestamp updatedAt;
    @Nullable
    private Product product;

    public CartItem() {}

    public CartItem(long id,
                    long cartId,
                    long productId,
                    int quantity,
                    Timestamp createdAt,
                    @Nullable Timestamp updatedAt) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Nullable
    @Nested("product")
    public Product getProduct() {
        return product;
    }

    public void setProduct(@Nullable Product product) {
        this.product = product;
    }
    public String getResource() {
        return "CartItem";
    }

    @Override
    public Timestamp getCreateAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", product=" + product +
                '}';
    }
}