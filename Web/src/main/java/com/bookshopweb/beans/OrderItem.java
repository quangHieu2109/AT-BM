package com.bookshopweb.beans;

import com.bookshopweb.dao.OrderItemDAO;
import com.bookshopweb.dao.ProductDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jdbi.v3.core.mapper.Nested;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.StringJoiner;

public class OrderItem extends AbsModel<OrderItem>{
    private long id;
    private long orderId;
    private long productId;
    private double price;
    private double discount;
    private int quantity;
    private Timestamp createdAt;
    @Nullable
    private Timestamp updatedAt;
    @Nullable
    private Product product;

    public OrderItem() {}

    public OrderItem(long id,
                     long orderId,
                     long productId,
                     double price,
                     double discount,
                     int quantity,
                     Timestamp createdAt,
                     @Nullable Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.discount = discount;
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
    public String getResource() {
        return "OrderItem";
    }
    public void setProduct(@Nullable Product product) {
        this.product = product;
    }

    public JsonObject getInfo() {
        ProductDAO productDAO = new ProductDAO();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("productId", this.productId);
        jsonObject.addProperty("quantity", this.quantity);
        jsonObject.addProperty("price", this.price);
        jsonObject.addProperty("discount", this.discount);

        return jsonObject;
    }
    public JsonObject getInfoForSwing() {
        ProductDAO productDAO = new ProductDAO();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("productId", this.productId);
        jsonObject.addProperty("quantity", this.quantity);
        jsonObject.addProperty("price", this.price);
        jsonObject.addProperty("discount", this.discount);
        jsonObject.add("product", JsonParser.parseString(new Gson().toJson(productDAO.getByIdProduct2(this.productId))));

        return jsonObject;
    }

    @Override
    public Timestamp getCreateAt() {
        return createdAt;
    }

    public static void main(String[] args) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderItem orderItem = orderItemDAO.selectPrevalue(1L);
        System.out.println(orderItem.toString());
    }
}