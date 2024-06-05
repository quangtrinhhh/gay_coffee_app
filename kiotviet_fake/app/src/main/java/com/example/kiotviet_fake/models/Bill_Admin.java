package com.example.kiotviet_fake.models;

public class Bill_Admin {
    public int id_bill;
    public String date_time;
    public  String dateTimeEnd;
    public  String code_admin;
    public int tableId;
    public int user_Id;
    public  double total_price;
    public String name_user;
    public String shop_id;

    public Bill_Admin(int id_bill, String date_time, String dateTimeEnd, String code_admin, int tableId, int user_Id, double total_price, String name_user, String shop_id) {
        this.id_bill = id_bill;
        this.date_time = date_time;
        this.dateTimeEnd = dateTimeEnd;
        this.code_admin = code_admin;
        this.tableId = tableId;
        this.user_Id = user_Id;
        this.total_price = total_price;
        this.name_user = name_user;
        this.shop_id = shop_id;
    }

    public int getId_bill() {
        return id_bill;
    }

    public void setId_bill(int id_bill) {
        this.id_bill = id_bill;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(String dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public String getCode_admin() {
        return code_admin;
    }

    public void setCode_admin(String code_admin) {
        this.code_admin = code_admin;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
