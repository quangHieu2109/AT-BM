package com.bookshopweb.beans;

public abstract class AbsAddress {
    private long id;
    private long addressId;
    private String name;
    private String code;

    public AbsAddress(long id, long addressId, String name, String code) {
        this.id = id;
        this.addressId = addressId;
        this.name = name;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
