package com.bookshopweb.beans;

public class OrderDetail {
    private long orderId;
    private long addressId;
    private long shipVoucherId;
    private double shipVoucherDecrease;
    private long productVoucherId;
    private double productVoucherDecrease;
    private double totalPrice;

    public OrderDetail(long orderId, long addressId, long shipVoucherId, double shipVoucherDecrease, long productVoucherId, double productVoucherDecrease, double totalPrice) {

        this.orderId = orderId;
        this.addressId = addressId;
        this.shipVoucherId = shipVoucherId;
        this.shipVoucherDecrease = shipVoucherDecrease;
        this.productVoucherId = productVoucherId;
        this.productVoucherDecrease = productVoucherDecrease;
        this.totalPrice = totalPrice;
    }

    public OrderDetail() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public long getShipVoucherId() {
        return shipVoucherId;
    }

    public void setShipVoucherId(long shipVoucherId) {
        this.shipVoucherId = shipVoucherId;
    }

    public double getShipVoucherDecrease() {
        return shipVoucherDecrease;
    }

    public void setShipVoucherDecrease(double shipVoucherDecrease) {
        this.shipVoucherDecrease = shipVoucherDecrease;
    }

    public long getProductVoucherId() {
        return productVoucherId;
    }

    public void setProductVoucherId(long productVoucherId) {
        this.productVoucherId = productVoucherId;
    }

    public double getProductVoucherDecrease() {
        return productVoucherDecrease;
    }

    public void setProductVoucherDecrease(double productVoucherDecrease) {
        this.productVoucherDecrease = productVoucherDecrease;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", addressId=" + addressId +
                ", shipVoucherId=" + shipVoucherId +
                ", shipVoucherDecrease=" + shipVoucherDecrease +
                ", productVoucherId=" + productVoucherId +
                ", productVoucherDecrease=" + productVoucherDecrease +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
