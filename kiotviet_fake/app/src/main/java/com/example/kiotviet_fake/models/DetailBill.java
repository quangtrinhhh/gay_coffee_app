package com.example.kiotviet_fake.models;

public class DetailBill {
    private int bill_item_id;
    private int quantity;
    private Double total_price;
    private int product_id;
    private  int bill_id;
    private  String  name;

    public DetailBill(int bill_item_id, int quantity, Double total_price, int product_id, int bill_id, String name) {
        this.bill_item_id = bill_item_id;
        this.quantity = quantity;
        this.total_price = total_price;
        this.product_id = product_id;
        this.bill_id = bill_id;
        this.name = name;
    }

    public int getBill_item_id() {
        return bill_item_id;
    }

    public void setBill_item_id(int bill_item_id) {
        this.bill_item_id = bill_item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
