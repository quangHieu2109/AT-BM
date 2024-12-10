package com.bookshopweb.api;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ViettelPostApi {
    private final String BASE_URL_ADDRESS = "https://partner.viettelpost.vn/v2/categories/";
    private final String BASE_URL_FEE = "https://partner.viettelpost.vn/v2/order/getPriceAll";
    private final String token = "F72D14C609C4C693ECDA34653EBCF032";
    public static final String HOA_TOC = "VHT";
    public static final String TIET_KIEM = "LCOD";
    public static final String NOI_TINH = "PHS";

    public static void main(String[] args) {
        ViettelPostApi viettelPostApi = new ViettelPostApi();
//        List<Province> provinces = viettelPostApi.getProvinces(null);
//        System.out.println(provinces);
        List<District> districts = viettelPostApi.getDistricts("2");
        System.out.println(districts);
        List<Ward> wards = viettelPostApi.getWards("1");
        System.out.println(wards);
        System.out.println(viettelPostApi.getInfoShips(500,5000,5000,"1","1","2","32",null));
    }

    public List<InfoShip> getInfoShips(int productWeight, int productPrice,int moneyCollection,String senderProvince, String senderDistrict, String receiverProvince, String receiverDistrict, @Nullable String typeVAT) {
        Connection connection = new Connection();
        HttpPost request = new HttpPost(BASE_URL_FEE);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("token", token);
        String json = "{\n" +
                "  \"SENDER_PROVINCE\" : " + senderProvince + ",\n" +
                "  \"SENDER_DISTRICT\" : " + senderDistrict + ",\n" +
                "  \"RECEIVER_PROVINCE\" : " + receiverProvince + ",\n" +
                "  \"RECEIVER_DISTRICT\" : " + receiverDistrict + ",\n" +
                "  \"PRODUCT_TYPE\" : \"HH\",\n" +
                "  \"PRODUCT_WEIGHT\" : " + productWeight + ",\n" +
                "  \"PRODUCT_PRICE\" : " + productPrice + ",\n" +
                "  \"MONEY_COLLECTION\" : \"" + moneyCollection + "\",\n" +
                "  \"TYPE\" :1\n" +
                "}";
        HttpEntity entity = new StringEntity(json, "UTF-8");
        request.setEntity(entity);
        connection.setRequest(request);
        connection.connect();
        String response = connection.getResponseString();
        System.out.println(response);
        JSONArray jsonArray = new JSONArray(response);
        return Arrays.asList(new Gson().fromJson(jsonArray.toString(), InfoShip[].class));

    }
    public class InfoShip{
        private String TEN_DICHVU;
        private String THOI_GIAN;
        private String GIA_CUOC;

        public InfoShip(String TEN_DICHVU, String THOI_GIAN, String GIA_CUOC) {

            this.TEN_DICHVU = TEN_DICHVU;
            this.THOI_GIAN = THOI_GIAN;
            this.GIA_CUOC = GIA_CUOC;
        }

        @Override
        public String toString() {
            return "InfoShip{" +
                    ", TEN_DICHVU='" + TEN_DICHVU + '\'' +
                    ", THOI_GIAN='" + THOI_GIAN + '\'' +
                    ", GIA_CUOC='" + GIA_CUOC + '\'' +
                    '}';
        }

        public String getTEN_DICHVU() {
            return TEN_DICHVU;
        }

        public void setTEN_DICHVU(String TEN_DICHVU) {
            this.TEN_DICHVU = TEN_DICHVU;
        }

        public String getTHOI_GIAN() {
            return THOI_GIAN;
        }

        public void setTHOI_GIAN(String THOI_GIAN) {
            this.THOI_GIAN = THOI_GIAN;
        }

        public String getGIA_CUOC() {
            return GIA_CUOC;
        }

        public void setGIA_CUOC(String GIA_CUOC) {
            this.GIA_CUOC = GIA_CUOC;
        }
    }
    public List<String> getTypeVAT(String provinceSenderID, String provinceReceiverID){
        if(provinceReceiverID.equals(provinceSenderID)){
            return Arrays.asList(HOA_TOC, NOI_TINH);
        }
        return Arrays.asList(HOA_TOC, TIET_KIEM);
    }
    public List<Province> getProvinces(@Nullable String provinceId) {
        if(provinceId== null || provinceId.isEmpty()){
            provinceId = "-1";
        }
        Connection connection = new Connection();
        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "listProvinceById?provinceId="+provinceId);
        connection.setRequest(request);
        connection.connect();
        String response = connection.getResponseString();
        JSONArray jsonObject = new JSONObject(response).getJSONArray("data");
        Province[] provinces = new Gson().fromJson(jsonObject.toString(), Province[].class);
        return Arrays.asList(provinces);
    }
    public List<District> getDistricts(@Nullable String provinceId) {
        if(provinceId== null || provinceId.isEmpty()){
            provinceId = "-1";
        }
        Connection connection = new Connection();
        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "listDistrict?provinceId=" + provinceId);
        connection.setRequest(request);
        connection.connect();
        String response = connection.getResponseString();
        JSONArray jsonObject = new JSONObject(response).getJSONArray("data");
        District[] districts = new Gson().fromJson(jsonObject.toString(), District[].class);
        return Arrays.asList(districts);
    }
    public List<Ward> getWards(@Nullable String districtId) {
        if(districtId== null || districtId.isEmpty()){
            districtId = "-1";
        }
        Connection connection = new Connection();
        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "listWards?districtId=" + districtId);
        connection.setRequest(request);
        connection.connect();
        String response = connection.getResponseString();
        JSONArray jsonObject = new JSONObject(response).getJSONArray("data");
        Ward[] wards = new Gson().fromJson(jsonObject.toString(), Ward[].class);
        return Arrays.asList(wards);
    }
    public class Ward {
        private String WARDS_ID;
        private String WARDS_NAME;
        private String DISTRICT_ID;

        public Ward(String WARDS_ID, String WARDS_NAME, String DISTRICT_ID) {
            this.WARDS_ID = WARDS_ID;
            this.WARDS_NAME = WARDS_NAME;
            this.DISTRICT_ID = DISTRICT_ID;
        }

        public String getWARDS_ID() {
            return WARDS_ID;
        }

        public void setWARDS_ID(String WARDS_ID) {
            this.WARDS_ID = WARDS_ID;
        }

        public String getWARDS_NAME() {
            return WARDS_NAME;
        }

        public void setWARDS_NAME(String WARDS_NAME) {
            this.WARDS_NAME = WARDS_NAME;
        }

        public String getDISTRICT_ID() {
            return DISTRICT_ID;
        }

        public void setDISTRICT_ID(String DISTRICT_ID) {
            this.DISTRICT_ID = DISTRICT_ID;
        }

        @Override
        public String toString() {
            return "Ward{" +
                    "WARDS_ID='" + WARDS_ID + '\'' +
                    ", WARDS_NAME='" + WARDS_NAME + '\'' +
                    ", DISTRICT_ID='" + DISTRICT_ID + '\'' +
                    '}';
        }
    }
    public class District{
        private String DISTRICT_ID;
        private String DISTRICT_VALUE;
        private String DISTRICT_NAME;
        private String PROVINCE_ID;

        public District(String DISTRICT_ID, String DISTRICT_VALUE, String DISTRICT_NAME, String PROVINCE_ID) {
            this.DISTRICT_ID = DISTRICT_ID;
            this.DISTRICT_VALUE = DISTRICT_VALUE;
            this.DISTRICT_NAME = DISTRICT_NAME;
            this.PROVINCE_ID = PROVINCE_ID;
        }

        public String getDISTRICT_ID() {
            return DISTRICT_ID;
        }

        public void setDISTRICT_ID(String DISTRICT_ID) {
            this.DISTRICT_ID = DISTRICT_ID;
        }

        public String getDISTRICT_VALUE() {
            return DISTRICT_VALUE;
        }

        public void setDISTRICT_VALUE(String DISTRICT_VALUE) {
            this.DISTRICT_VALUE = DISTRICT_VALUE;
        }

        public String getDISTRICT_NAME() {
            return DISTRICT_NAME;
        }

        public void setDISTRICT_NAME(String DISTRICT_NAME) {
            this.DISTRICT_NAME = DISTRICT_NAME;
        }

        public String getPROVINCE_ID() {
            return PROVINCE_ID;
        }

        public void setPROVINCE_ID(String PROVINCE_ID) {
            this.PROVINCE_ID = PROVINCE_ID;
        }

        @Override
        public String toString() {
            return "District{" +
                    "DISTRICT_ID='" + DISTRICT_ID + '\'' +
                    ", DISTRICT_VALUE='" + DISTRICT_VALUE + '\'' +
                    ", DISTRICT_NAME='" + DISTRICT_NAME + '\'' +
                    ", PROVINCE_ID='" + PROVINCE_ID + '\'' +
                    '}';
        }
    }

    public class Province {
        private String PROVINCE_ID;
        private String PROVINCE_NAME;
        private String PROVINCE_CODE;

        public Province(String PROVINCE_ID, String PROVINCE_NAME, String PROVINCE_CODE) {
            this.PROVINCE_ID = PROVINCE_ID;
            this.PROVINCE_NAME = PROVINCE_NAME;
            this.PROVINCE_CODE = PROVINCE_CODE;
        }

        public String getPROVINCE_ID() {
            return PROVINCE_ID;
        }

        public void setPROVINCE_ID(String PROVINCE_ID) {
            this.PROVINCE_ID = PROVINCE_ID;
        }

        public String getPROVINCE_NAME() {
            return PROVINCE_NAME;
        }

        public void setPROVINCE_NAME(String PROVINCE_NAME) {
            this.PROVINCE_NAME = PROVINCE_NAME;
        }

        public String getPROVINCE_CODE() {
            return PROVINCE_CODE;
        }

        public void setPROVINCE_CODE(String PROVINCE_CODE) {
            this.PROVINCE_CODE = PROVINCE_CODE;
        }

        @Override
        public String toString() {
            return "Province{" +
                    "PROVINCE_ID='" + PROVINCE_ID + '\'' +
                    ", PROVINCE_NAME='" + PROVINCE_NAME + '\'' +
                    ", PROVINCE_CODE='" + PROVINCE_CODE + '\'' +
                    '}';
        }
    }


}
