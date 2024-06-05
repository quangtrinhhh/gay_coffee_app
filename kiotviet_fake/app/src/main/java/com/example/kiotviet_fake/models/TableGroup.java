package com.example.kiotviet_fake.models;

public class TableGroup {
    public String User_id;
    public int table_id;
    public String table_name;
    public int status;
    public String table_price;
    public  int product_quantity;
    public  int order_id;

    public TableGroup(String user_id, int table_id, String table_name, int status, String table_price, int product_quantity, int order_id) {
        User_id = user_id;
        this.table_id = table_id;
        this.table_name = table_name;
        this.status = status;
        this.table_price = table_price;
        this.product_quantity = product_quantity;
        this.order_id = order_id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
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

    public String getTable_price() {
        return table_price;
    }

    public void setTable_price(String table_price) {
        this.table_price = table_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
