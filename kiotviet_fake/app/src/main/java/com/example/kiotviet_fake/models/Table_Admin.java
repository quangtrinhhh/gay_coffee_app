package com.example.kiotviet_fake.models;

public class Table_Admin {
    private  String id_shop;
    private  String id_table;
    private  String table_name;
    private  String table_price;
    private  int status;


    public Table_Admin(String id_shop, String id_table, String table_name, String table_price, int status) {
        this.id_shop = id_shop;
        this.id_table = id_table;
        this.table_name = table_name;
        this.table_price = table_price;
        this.status = status;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }

    public String getId_table() {
        return id_table;
    }

    public void setId_table(String id_table) {
        this.id_table = id_table;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_price() {
        return table_price;
    }

    public void setTable_price(String table_price) {
        this.table_price = table_price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
