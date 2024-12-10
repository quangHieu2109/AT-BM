package com.bookshopweb.beans;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

public class AccurancyUser {
    private long id;
    private String userName;
    private String accurancyCode;
    private Timestamp endAt;

    public AccurancyUser(long id, String userName, String accurancyCode, Timestamp endAt) {
        this.id = id;
        this.userName = userName;
        this.accurancyCode = accurancyCode;
        this.endAt = endAt;
    }

    public AccurancyUser(String userName, String accurancyCode, Timestamp endAt) {
        this.userName = userName;
        this.accurancyCode = accurancyCode;
        this.endAt = endAt;
    }
    public AccurancyUser(String userName) {
        this.userName = userName;
        this.endAt = new Timestamp(Calendar.getInstance().getTimeInMillis() + 300000);
        createAccurancyCode();
    }

    public AccurancyUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccurancyCode() {
        return accurancyCode;
    }

    public void setAccurancyCode(String accurancyCode) {
        this.accurancyCode = accurancyCode;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }
    public String createAccurancyCode(){
        String result="";
        for(int i=0; i<6; i++){
            result += new Random().nextInt(9);
        }
        setAccurancyCode(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new AccurancyUser().createAccurancyCode());
    }
}
