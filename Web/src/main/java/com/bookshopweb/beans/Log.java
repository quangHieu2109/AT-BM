package com.bookshopweb.beans;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

public class Log {
    private long id;
    private String ip="";
    private int levelLog;
    private String res ="";
    private  String preValue="";
    private   String curValue="";
    private Timestamp createAt;
    private Timestamp updateAt;

    public Log() {
    }

    public Log(long id, String ip, int levelLog, String res, String preValue, String curValue, Timestamp createAt, Timestamp updateAt) {
        this.id = id;
        this.ip = ip;
        this.levelLog = levelLog;
        this.res = res;
        this.preValue = preValue;
        this.curValue = curValue;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    public Log(String ip, int levelLog, String res, String preValue, String curValue, Timestamp createAt) {
        long id = Calendar.getInstance().getTimeInMillis() + new Random().nextInt(100)*1000;
        this.id = id;
        this.ip = ip;
        this.levelLog = levelLog;
        this.res = res;
        this.preValue = preValue;
        this.curValue = curValue;
        this.createAt = createAt;
        this.updateAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLevelLog(int levelLog) {
        this.levelLog = levelLog;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    public void setCurValue(String curValue) {
        this.curValue = curValue;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getLevelLog() {
        return levelLog;
    }

    public String getRes() {
        return res;
    }

    public String getPreValue() {
        return preValue;
    }

    public String getCurValue() {
        return curValue;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", levelLog=" + levelLog +
                ", resource='" + res + '\'' +
                ", preValue='" + preValue + '\'' +
                ", curValue='" + curValue + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

}