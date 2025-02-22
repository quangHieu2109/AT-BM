package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    private long id;
    private long userId;
    private String createdAt;
    private double totalPrice;
    private Address delivery_address;
    @SerializedName("OrderItems")
    private List<OrderItem> orderItems;

    public Order(long id, long userId, String createdAt, double totalPrice, Address delivery_address, List<OrderItem> orderItems) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.delivery_address = delivery_address;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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
    public String getInfo() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("userId", this.userId);
        jsonObject.addProperty("createdAt", this.createdAt.toString());
        jsonObject.addProperty("totalPrice", this.totalPrice);
        jsonObject.add("delivery_address", delivery_address.getInfo());

        JsonArray jsonArray = new JsonArray();
        for(OrderItem item: orderItems){
            jsonArray.add(item.getInfo());
        }
        jsonObject.add("OrderItems", jsonArray);

        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                ", totalPrice=" + totalPrice +
                ", delivery_address=" + delivery_address +
                ", orderItems=" + orderItems +
                '}';
    }
}
