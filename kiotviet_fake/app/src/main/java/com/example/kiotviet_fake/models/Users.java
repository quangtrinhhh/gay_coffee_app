package com.example.kiotviet_fake.models;

public class Users {
    private String shop_id;
    private String user_id;
    private String shop_name;
    private String user_name;
    private String password;
    private String role;

    public Users(String shop_id, String user_id, String shop_name, String user_name, String password, String role) {
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.shop_name = shop_name;
        this.user_name = user_name;
        this.password = password;
        this.role = role;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
