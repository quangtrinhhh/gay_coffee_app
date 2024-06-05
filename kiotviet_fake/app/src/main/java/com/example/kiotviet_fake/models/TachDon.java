package com.example.kiotviet_fake.models;

public class TachDon {
    private int tableId;
    private int newQuantity;
    private int quantity;
    private float totalPrice;
    private int productId;
    private float priceProduct;
    private String nameProduct;
    private int idOrderItem;

    public TachDon(int tableId, int newQuantity, int quantity, float totalPrice, int productId, float priceProduct, String nameProduct, int idOrderItem) {
        this.tableId = tableId;
        this.newQuantity = newQuantity;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productId = productId;
        this.priceProduct = priceProduct;
        this.nameProduct = nameProduct;
        this.idOrderItem = idOrderItem;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(float priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(int idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    @Override
    public String toString() {
        return "TachDon{" +
                "tableId=" + tableId +
                ", newQuantity=" + newQuantity +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", productId=" + productId +
                ", priceProduct=" + priceProduct +
                ", nameProduct=" + nameProduct +
                ", idOrderItem=" + idOrderItem +
                '}';
    }
}
