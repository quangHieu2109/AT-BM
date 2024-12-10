package com.bookshopweb.beans;

public class CategorysOfVoucher {
    private long id;
    private long voucherId;
    private long categoryId;

    public CategorysOfVoucher(long id, long voucherId, long categoryId) {

        this.id = id;
        this.voucherId = voucherId;
        this.categoryId = categoryId;
    }

    public CategorysOfVoucher() {
    }

    @Override
    public String toString() {
        return "CategorysOfVoucher{" +
                "id=" + id +
                ", voucherId=" + voucherId +
                ", categoryId=" + categoryId +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(long voucherId) {
        this.voucherId = voucherId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
