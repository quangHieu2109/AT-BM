package com.bookshopweb.beans;

import java.sql.Timestamp;

public class User extends AbsModel<User> {
    private long id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phoneNumber;
    private int gender;

    private String role;
    private Timestamp createAt;
    private boolean googleUser = false;
    private boolean accuracy = true;

    public User() {
    }



    public User(long id, String username, String password, String fullname, String email, String phoneNumber, int gender, String role, Timestamp createAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.role = role;
        this.createAt = createAt;
    }


    public boolean isAccuracy() {
        return accuracy;
    }

    public void setAccuracy(boolean accuracy) {
        this.accuracy = accuracy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", role='" + role + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    @Override
    public String getResource() {
        return "User";
    }

    @Override
    public Timestamp getCreateAt() {
        return createAt;
    }

    public boolean isGoogleUser() {
        return googleUser;
    }

    public boolean isCustomer() {
        return role.equalsIgnoreCase("CUSTOMER");
    }

    public void setGoogleUser(boolean googleUser) {
        this.googleUser = googleUser;
    }
}