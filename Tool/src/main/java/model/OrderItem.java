package model;

import com.google.gson.JsonObject;

public class OrderItem {
    private long id;
    private long productId;
    private long quantity;
    private double price;
    private double discount;
    private Product product;  // Đây là đối tượng Product chứa thông tin về sản phẩm

    // Constructor
    public OrderItem(long id, long productId, long quantity, double price, double discount, Product product) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return product != null ? product.getName() : "Unknown Product";
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }
    public JsonObject getInfo() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("productId", this.productId);
        jsonObject.addProperty("quantity", this.quantity);
        jsonObject.addProperty("price", this.price);
        jsonObject.addProperty("discount", this.discount);

        return jsonObject;
    }

}
