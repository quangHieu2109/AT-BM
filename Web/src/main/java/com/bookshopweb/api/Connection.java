package com.bookshopweb.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Connection {
    private HttpResponse response;
    private HttpEntity entity;
    private HttpUriRequest request;
    private String baseUrl = "";
    private HttpClient client;
    @Deprecated
    public Connection(String baseUrl) {
        client = HttpClients.createDefault();
        this.baseUrl = baseUrl;
    }
    public Connection() {
        client = HttpClients.createDefault();

    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public void setRequest(HttpUriRequest request) {
        this.request = request;
    }
    public void connect(){
        try {
            response = client.execute(request);
            entity = response.getEntity();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getResponseString(){
        try {
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public HttpEntity getEntity() {
        return entity;
    }
    public HttpResponse getResponse() {
        return response;
    }


}
