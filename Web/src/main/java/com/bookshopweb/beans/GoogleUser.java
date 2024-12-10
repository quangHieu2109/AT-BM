package com.bookshopweb.beans;

public class GoogleUser {
    private String email;
    private long id;

    public GoogleUser(String email, long id) {

        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    @Override
    public String toString() {
        return "GoogleUser{" +
                "email='" + email + '\'' +
                ", id=" + id +
                '}';
    }
}
