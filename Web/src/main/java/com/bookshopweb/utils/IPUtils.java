package com.bookshopweb.utils;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.http.HttpHost;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
    private static final String API_URL = "https://api.myip.com";
    private static final String API_INFO = "https://ipinfo.io/";
    private static final String TOKEN = "dab86b0df924a7";

    public static IPInfo getIPInfo(String ip) {

        HttpGet get = new HttpGet(API_INFO+ip+"?token="+TOKEN);
        IPInfo ipInfo = null;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpResponse httpResponse = httpClient.execute(get);
            String response = EntityUtils.toString(httpResponse.getEntity()).trim();
            Gson gson = new Gson();
            ipInfo = gson.fromJson(response, IPInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipInfo;
    }

    public static IPInfo getIPInfo(HttpServletRequest request) {
        return getIPInfo(getIP(request));
    }


    public static String getIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        HttpGet get = new HttpGet(API_URL);
        try {


            if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
                CloseableHttpClient httpClient = HttpClients.createDefault();

                HttpResponse httpResponse = httpClient.execute(get);
                String data = EntityUtils.toString(httpResponse.getEntity()).trim();
                JSONObject jsonObject = new JSONObject(data);
                ip = jsonObject.getString("ip");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public class IPInfo {
        String ip;
        String city;
        String region;
        String country;
        String loc;
        String org;
        String postal;
        String timezone;

        public IPInfo(String ip, String city, String region, String country, String loc, String org, String postal, String timezone) {
            this.ip = ip;
            this.city = city;
            this.region = region;
            this.country = country;
            this.loc = loc;
            this.org = org;
            this.postal = postal;
            this.timezone = timezone;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getPostal() {
            return postal;
        }

        public void setPostal(String postal) {
            this.postal = postal;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        @Override
        public String toString() {
            return "IPInfo{" +
                    "ip='" + ip + '\'' +
                    ", city='" + city + '\'' +
                    ", region='" + region + '\'' +
                    ", country='" + country + '\'' +
                    ", loc='" + loc + '\'' +
                    ", org='" + org + '\'' +
                    ", postal='" + postal + '\'' +
                    ", timezone='" + timezone + '\'' +
                    '}';
        }
    }
}
