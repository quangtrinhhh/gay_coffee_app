package com.example.kiotviet_fake.models;

import java.util.Date;

public class History {
    private int id;
    private String dateTime;
    private String dateTime_end;
    private String code;
    private int table_id;
    private int user_id;
    private double total_price;
    private String nameTable;

    public History(int id, String dateTime, String dateTime_end, String code, int table_id, int user_id, double total_price, String nameTable) {
        this.id = id;
        this.dateTime = dateTime;
        this.dateTime_end = dateTime_end;
        this.code = code;
        this.table_id = table_id;
        this.user_id = user_id;
        this.total_price = total_price;
        this.nameTable = nameTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime_end() {
        return dateTime_end;
    }

    public void setDateTime_end(String dateTime_end) {
        this.dateTime_end = dateTime_end;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }
}
