package com.bookshopweb.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CaptchaApi {
    private static CaptchaApi instance = new CaptchaApi();
    private static Connection connection = new Connection();
    public static boolean checkCaptcha(String responseCaptcha){
        HttpPost postRequest = new HttpPost("https://www.google.com/recaptcha/api/siteverify");
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("secret", "6LcFrfEpAAAAAP2JT_hYejaSvr8HNnlyXAHOXJhP"));
        parameters.add(new BasicNameValuePair("response", responseCaptcha));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            postRequest.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        connection.setRequest(postRequest);
        connection.connect();
        String response = connection.getResponseString();
        JSONObject jsonObject = new JSONObject(response);

        return jsonObject.getBoolean("success");
    }
    public static CaptchaApi getInstance() {
        return instance;
    }
}
