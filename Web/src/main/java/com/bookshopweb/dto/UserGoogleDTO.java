package com.bookshopweb.dto;

public class UserGoogleDTO {
    private String id;
    private String email;
    private String name;
    private String picture;
    private String family_name;
    private String given_name;
    private String locale;

    public UserGoogleDTO(String id, String email, String name, String picture, String family_name, String given_name,String locale) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.family_name = family_name;
        this.given_name = given_name;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    @Override
    public String toString() {
        return "UserGoogleDTO{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", family_name='" + family_name + '\'' +
                ", given_name='" + given_name + '\'' +
                '}';
    }
}
