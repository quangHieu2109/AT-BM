package model;

public class Address {
    private long id;
    private String province;
    private String district;
    private String ward;
    private String houseNumber;

    public Address(long id, String province, String district, String ward, String houseNumber) {
        this.id = id;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.houseNumber = houseNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getText(){
        return houseNumber + ", " + ward + ", " + district + ", " + province;
    }
}
