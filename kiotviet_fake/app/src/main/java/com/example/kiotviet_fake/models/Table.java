package com.example.kiotviet_fake.models;

public class Table {
    public int id;
    public String table_name;
    public int status;
    public int user_id;
    public String table_price;

    public Table(int id, String table_name, int status, int user_id, String table_price) {
        this.id = id;
        this.table_name = table_name;
        this.status = status;
        this.user_id = user_id;
        this.table_price = table_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTable_price() {
        return table_price;
    }

    public void setTable_price(String table_price) {
        this.table_price = table_price;
    }
}
