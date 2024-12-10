package com.bookshopweb.api;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class GHNApi {
    public Connection connection;
    public static final String BASE_URL_FEE = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/";
    public static final String BASE_URL_ADDRESS = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/";
    private static final String TOKEN = "3bc139c0-ee96-11ee-8bfa-8a2dda8ec551";
    private static final String SHOP_ID = "191635";




    public static void main(String[] args) {
        GHNApi ghnApi = new GHNApi();
//        System.out.println(ghnApi.getWards(3695));
        System.out.println(ghnApi.getInfoShip(3695,"90737",3695,"90737",520,10,10,10));

    }
    public List<InfoShip> getInfoShip(int fromDistrictID, String fromWardCode, int toDistrictID, String toWardCode, int weight, int height, int length, int width){
        List<InfoShip> infoShips = new ArrayList<>();
        List<Service> services = getServiceType(String.valueOf(fromDistrictID),String.valueOf(toDistrictID));
//        System.out.println(services);
        for (Service service : services) {
            if(service.getService_type_id() == 5) {
                continue;
            }
            long fee = getFeeFromTo(
                    fromDistrictID,
                    fromWardCode,
                    toDistrictID,
                    toWardCode,
                    weight,
                    height,
                    length,
                    width,
                    String.valueOf(service.getService_id())
            );
//            System.out.println(fee);

            String estimatedTime = getEstimatedTime(fromDistrictID,fromWardCode,toDistrictID,toWardCode,service.getService_id());

            infoShips.add(new InfoShip(service.getShort_name(),estimatedTime,String.valueOf(fee)));
        }
        return infoShips;
    }
    public String getEstimatedTime(int fromDistrictID,String fromWardCode, int toDistrictID, String toWardCode, int serviceID){
        HttpPost request = new HttpPost(BASE_URL_FEE + "shipping-order/leadtime");
        request.setHeader("Token", TOKEN);
        request.setHeader("ShopId", SHOP_ID);
        request.setHeader("Content-Type", "application/json");

        String json = "{" +
                "            \"from_district_id\":" + fromDistrictID + ",\n" +
                "            \"from_ward_code\":\"" + fromWardCode + "\",\n" +
                "            \"to_district_id\":" + toDistrictID + ",\n" +
                "            \"to_ward_code\":\"" + toWardCode + "\",\n" +
                "            \"service_id\":" + serviceID + "\n" +
                "            }";
        HttpEntity enity = new StringEntity(json, "UTF-8");
        request.setEntity(enity);
        connection.setRequest(request);
        connection.connect();
//        System.out.println(connection.getResponseString());

        long date = new JSONObject(connection.getResponseString()).getJSONObject("data").getLong("leadtime");
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        return dateTime.toString().replace("T"," ");

    }
    private List<Service> getServiceType(String fromDistrict, String toDistrict) {
        HttpPost request = new HttpPost(BASE_URL_FEE + "shipping-order/available-services");
        request.setHeader("Token", TOKEN);
        request.setHeader("Content-Type", "application/json");
        String json = "{\n" +
                "\t\"shop_id\":"+SHOP_ID+",\n" +
                "\t\"from_district\": "+fromDistrict+",\n" +
                "\t\"to_district\": "+toDistrict+"\n" +
                "}";
        HttpEntity enity = new StringEntity(json, "UTF-8");
        request.setEntity(enity);
        connection.setRequest(request);
        connection.connect();
        JSONArray jsonObject = new JSONObject(connection.getResponseString()).getJSONArray("data");
        Gson gson = new Gson();

        return Arrays.asList(gson.fromJson(jsonObject.toString(), Service[].class));

    }

    public GHNApi() {
        connection = new Connection();
    }

    public GHNApi(String baseURL) {
        connection = new Connection(baseURL);
    }

    public void setBaseURL(String baseURL) {
        connection.setBaseUrl(baseURL);
    }

    public long getFeeFromTo(int fromDistrictID, String fromWardCode, int toDistrictID, String toWardCode, int weight, int height, int length, int width, String serviceID) {
        HttpPost request = new HttpPost(BASE_URL_FEE + "shipping-order/fee");
        request.setHeader("Token", TOKEN);
        request.setHeader("ShopId", SHOP_ID);
        request.setHeader("Content-Type", "application/json");

        String json = "{" +
                "            \"from_district_id\":" + fromDistrictID + ",\n" +
                "            \"from_ward_code\":\"" + fromWardCode + "\",\n" +
                "            \"to_district_id\":" + toDistrictID + ",\n" +
                "            \"to_ward_code\":\"" + toWardCode + "\",\n" +
                "            \"service_id\":" + serviceID + ",\n" +
                "            \"height\":" + height + ",\n" +
                "            \"length\":" + length + ",\n" +
                "            \"weight\":" + weight + ",\n" +
                "            \"width\":" + width + ",\n" +
                "            \"insurance_value\":0,\n" +
                "            \"cod_failed_amount\":0,\n" +
                "            \"coupon\": null\n" +
                "            }";
        HttpEntity enity = new StringEntity(json, "UTF-8");
        request.setEntity(enity);
        connection.setRequest(request);
        connection.connect();


        return new JSONObject(connection.getResponseString()).getJSONObject("data").getLong("total");
    }

    public List<Province> getProvinces() {
        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "province");
        request.setHeader("Token", TOKEN);
        connection.setRequest(request);
        connection.connect();

        Gson gson = new Gson();
        Province[] province = gson.fromJson(new JSONObject(connection.getResponseString()).getJSONArray("data").toString(), Province[].class);
        return Arrays.asList(province);
    }

    public List<District> getDistricts(int provinceID) {


        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "district?province_id=" + String.valueOf(provinceID));

        request.setHeader("Token", TOKEN);
        request.setHeader("province_id", String.valueOf(provinceID));
        connection.setRequest(request);
        connection.connect();

        Gson gson = new Gson();
        District[] district = gson.fromJson(new JSONObject(connection.getResponseString()).getJSONArray("data").toString(), District[].class);
        return Arrays.asList(district);
    }

    public List<Ward> getWards(String districtID) {


        HttpGet request = new HttpGet(BASE_URL_ADDRESS + "ward?district_id=" + districtID);

        request.setHeader("Token", TOKEN);
        request.setHeader("district_id", String.valueOf(districtID));
        request.setHeader("Content-Type", "application/json");
        connection.setRequest(request);
        connection.connect();

        Gson gson = new Gson();
        Ward[] ward= gson.fromJson(new JSONObject(connection.getResponseString()).getJSONArray("data").toString(), Ward[].class);
        return Arrays.asList(ward);
    }

    public class Ward {
        private String WardCode;
        private String DistrictID;
        private String WardName;


        public Ward(String wardCode, String districtID, String wardName) {
            WardCode = wardCode;
            DistrictID = districtID;
            WardName = wardName;
        }

        public String getWardCode() {
            return WardCode;
        }

        public void setWardCode(String wardCode) {
            WardCode = wardCode;
        }

        public String getDistrictID() {
            return DistrictID;
        }

        public void setDistrictID(String districtID) {
            DistrictID = districtID;
        }

        public String getWardName() {
            return WardName;
        }

        public void setWardName(String wardName) {
            WardName = wardName;
        }

        @Override
        public String toString() {
            return "Ward{" +
                    "WardCode=" + WardCode +
                    ", DistrictID=" + DistrictID +
                    ", WardName='" + WardName + '\'' +
                    '}';
        }
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
   public class District {
        private int DistrictID;
        private String DistrictName;
        private int ProvinceID;
        private String Code;


        public District(int districtID, String districtName, int provinceID, String code) {
            DistrictID = districtID;
            DistrictName = districtName;
            ProvinceID = provinceID;
            Code = code;

        }

        public int getDistrictID() {
            return DistrictID;
        }

        public void setDistrictID(int districtID) {
            DistrictID = districtID;
        }

        public String getDistrictName() {
            return DistrictName;
        }

        public void setDistrictName(String districtName) {
            DistrictName = districtName;
        }

        public int getProvinceID() {
            return ProvinceID;
        }

        public void setProvinceID(int provinceID) {
            ProvinceID = provinceID;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }


        @Override
        public String toString() {
            return "District{" +
                    "DistrictID=" + DistrictID +
                    ", DistrictName='" + DistrictName + '\'' +
                    ", ProvinceID=" + ProvinceID +
                    ", Code=" + Code +
                    '}';
        }
    }

    public class Province {
        private int ProvinceID;
        private String ProvinceName;
        private int CountryID;
        private int Code;
        private int IsEnable;
        private int RegionID;

        public Province(int provinceID, String provinceName, int countryID, int code, int isEnable, int regionID) {
            ProvinceID = provinceID;
            ProvinceName = provinceName;
            CountryID = countryID;
            Code = code;
            IsEnable = isEnable;
            RegionID = regionID;
        }

        public int getProvinceID() {
            return ProvinceID;
        }

        public void setProvinceID(int provinceID) {
            ProvinceID = provinceID;
        }

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String provinceName) {
            ProvinceName = provinceName;
        }

        public int getCountryID() {
            return CountryID;
        }

        public void setCountryID(int countryID) {
            CountryID = countryID;
        }

        public int getCode() {
            return Code;
        }

        public void setCode(int code) {
            Code = code;
        }

        public int getIsEnable() {
            return IsEnable;
        }

        public void setIsEnable(int isEnable) {
            IsEnable = isEnable;
        }

        public int getRegionID() {
            return RegionID;
        }

        public void setRegionID(int regionID) {
            RegionID = regionID;
        }

        @Override
        public String toString() {
            return "Province{" +
                    "ProvinceID=" + ProvinceID +
                    ", ProvinceName='" + ProvinceName + '\'' +
                    ", CountryID=" + CountryID +
                    ", Code=" + Code +
                    ", IsEnable=" + IsEnable +
                    ", RegionID=" + RegionID +
                    '}';
        }
    }
    public class Service{
        private int service_id;
        private String short_name;
        private int service_type_id;

        public Service(int service_id, String short_name, int service_type_id) {
            this.service_id = service_id;
            this.short_name = short_name;
            this.service_type_id = service_type_id;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public int getService_type_id() {
            return service_type_id;
        }

        public void setService_type_id(int service_type_id) {
            this.service_type_id = service_type_id;
        }

        @Override
        public String toString() {
            return "Service{" +
                    "service_id=" + service_id +
                    ", short_name='" + short_name + '\'' +
                    ", service_type_id=" + service_type_id +
                    '}';
        }
    }

}
