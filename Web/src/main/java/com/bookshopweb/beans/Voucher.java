package com.bookshopweb.beans;

import java.sql.Timestamp;
import java.util.Calendar;

public class Voucher extends AbsModel<Voucher> {
    private long id;
    private String voucherCode;
    private String voucherName;
    private String description;
    private int quantity;
    private double percentDecrease;
    private double maxDecrease;
    private double minPrice;
    private int type;
    private Timestamp startAt;
    private Timestamp endAt;
    private Timestamp createAt;
    private String voucherImage;

    public Voucher(long id, String voucherCode, String voucherName, String description, int quantity, double percentDecrease, double maxDecrease, double minPrice, int type, Timestamp startAt, Timestamp endAt, String voucherImage) {
        this.id = id;
        this.voucherCode = voucherCode;
        this.voucherName = voucherName;
        this.description = description;
        this.quantity = quantity;
        this.percentDecrease = percentDecrease;
        this.maxDecrease = maxDecrease;
        this.minPrice = minPrice;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.voucherImage = voucherImage;
        this.createAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public Voucher() {
    }

    @Override
    public String getResource() {
        return "Voucher";
    }

    @Override
    public Timestamp getCreateAt() {
        return createAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPercentDecrease() {
        return percentDecrease;
    }

    public void setPercentDecrease(double percentDecrease) {
        this.percentDecrease = percentDecrease;
    }

    public double getMaxDecrease() {
        return maxDecrease;
    }

    public void setMaxDecrease(double maxDecrease) {
        this.maxDecrease = maxDecrease;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", voucherCode='" + voucherCode + '\'' +
                ", voucherName='" + voucherName + '\'' +
                ", description='" + description + '\'' +
                ", percentDecrease=" + percentDecrease +
                ", maxRecrease=" + maxDecrease +
                ", type=" + type +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", image='" + voucherImage + '\'' +
                '}';
    }
}
