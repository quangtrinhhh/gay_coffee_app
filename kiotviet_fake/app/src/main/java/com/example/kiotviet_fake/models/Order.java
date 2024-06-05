package com.example.kiotviet_fake.models;

public class Order {
    private String idProductItem;
    private int quantity;
    private String price;
    private int orderId;
    private int productId;

    public Order(String idProductItem, int quantity, String price, int orderId, int productId) {
        this.idProductItem = idProductItem;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.productId = productId;
    }

    public String getIdProductItem() {
        return idProductItem;
    }

    public void setIdProductItem(String idProductItem) {
        this.idProductItem = idProductItem;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idProductItem='" + idProductItem + '\'' +
                ", quantity=" + quantity +
                ", price='" + price + '\'' +
                ", orderId=" + orderId +
                ", productId=" + productId +
                '}';
    }
}