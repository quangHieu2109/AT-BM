package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    private long id;
    private long userId;
    private String createAt;
    private double totalPrice;
    private Address delivery_address;
    @SerializedName("OrderItems")
    private List<OrderItem> orderItems;

    public Order(long id, long userId, String createAt, double totalPrice, Address delivery_address, List<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.createAt = createAt;
        this.totalPrice = totalPrice;
        this.delivery_address = delivery_address;
        this.orderItems = orderItems;
    }

    public Address getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(Address delivery_address) {
        this.delivery_address = delivery_address;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
