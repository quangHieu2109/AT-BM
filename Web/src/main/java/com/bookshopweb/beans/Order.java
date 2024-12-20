package com.bookshopweb.beans;

import com.bookshopweb.dao.AddressDAO;
import com.bookshopweb.dao.OrderDAO;
import com.bookshopweb.dao.OrderItemDAO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;
import java.util.StringJoiner;

public class Order extends AbsModel<Order> {
    private long id;
    private long userId;
    private int status;
    private int deliveryMethod;
    private double deliveryPrice;
    private Timestamp createdAt;
    @Nullable
    private Timestamp updatedAt;
    @Nullable
    private User user;
    @Nullable
    private List<OrderItem> orderItems;
    private double totalPrice;

    public Order() {}

    public Order(long id,
                 long userId,
                 int status,
                 int deliveryMethod,
                 double deliveryPrice,
                 Timestamp createdAt,
                 @Nullable Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.deliveryMethod = deliveryMethod;
        this.deliveryPrice = deliveryPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
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
    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    @Nullable
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(@Nullable List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getResource() {
        return "Order";
    }

    public String getInfo() {
        Address address = new AddressDAO().selectByOrder(this.id);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("userId", this.userId);
        jsonObject.addProperty("createdAt", this.createdAt.toString());
        jsonObject.addProperty("totalPrice", this.totalPrice);
        jsonObject.add("delivery_address", address.getInfo());

        JsonArray jsonArray = new JsonArray();
        for(OrderItem item: new OrderItemDAO().getByOrderId(this.id)){
            jsonArray.add(item.getInfo());
        }
        jsonObject.add("OrderItems", jsonArray);

        return jsonObject.toString();
    }
    public String getInfoForSwing() {
        Address address = new AddressDAO().selectByOrder(this.id);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("userId", this.userId);
        jsonObject.addProperty("createdAt", this.createdAt.toString());
        jsonObject.addProperty("totalPrice", this.totalPrice);
        jsonObject.add("delivery_address", address.getInfo());

        JsonArray jsonArray = new JsonArray();
        for(OrderItem item: new OrderItemDAO().getByOrderId(this.id)){
            jsonArray.add(item.getInfoForSwing());
        }
        jsonObject.add("OrderItems", jsonArray);

        return jsonObject.toString();
    }

    @Override
    public Timestamp getCreateAt() {
        return createdAt;
    }

    public static void main(String[] args) {
       for(Order order: new OrderDAO().getUnconfirmOrdersByUsername("user1")){
           System.out.println(JsonParser.parseString(order.getInfo()));
           break;
       }
    }

}