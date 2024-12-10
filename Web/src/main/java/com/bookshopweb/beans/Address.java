package com.bookshopweb.beans;

public class Address {
    private long id;
    private long userId;
    private String province;
    private String district;
    private String ward;

    private String houseNumber;

    public Address(long id, long userId, String province, String district, String ward, String houseNumber) {
        this.id = id;
        this.userId = userId;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.houseNumber = houseNumber;
    }

    public Address(long id, long userId, String houseNumber) {
        this.id = id;
        this.userId = userId;
        this.houseNumber = houseNumber;
    }
    public Address( long userId, String houseNumber) {
        this.userId = userId;
        this.houseNumber = houseNumber;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
