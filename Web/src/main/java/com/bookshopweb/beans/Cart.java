package com.bookshopweb.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Cart extends AbsModel<Cart>{
    private long id;
    private long userId;
    private Timestamp createdAt;
    @Nullable
    private Timestamp updatedAt;
    private List<CartItem> listCartItem = new ArrayList<>();
    public Cart() {}

    public Cart(long id,
                long userId,
                Timestamp createdAt,
                @Nullable Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public void addCartItem(CartItem cartItem){
        this.listCartItem.add(cartItem);
    }
    public void addCartItem(List<CartItem> cartItems){
        this.listCartItem.addAll(cartItems);
    }
    public List<CartItem> getListCartItem(){
        return this.listCartItem;
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

    @Override
    public String getResource() {
        return "Cart";
    }

    @Override
    public Timestamp getCreateAt() {
        return createdAt;
    }
}