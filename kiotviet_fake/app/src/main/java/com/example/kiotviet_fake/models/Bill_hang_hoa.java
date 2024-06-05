package com.example.kiotviet_fake.models;

public class Bill_hang_hoa {
    private String name;
    private int quantity;
    private String price;

    public Bill_hang_hoa(String name, int quantity, String price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
