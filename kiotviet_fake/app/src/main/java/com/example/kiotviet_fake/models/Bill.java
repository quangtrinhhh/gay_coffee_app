package com.example.kiotviet_fake.models;

public class Bill {
    private String dateTime;
    private String dateTimeEnd;
    private String code;
    private int tableId;
    private int userId;
    private int quantity;
    private float totalPrice;
    private int productId;
    private String nameProduct;
    private String nameTable;
    private float priceProduct;
    private int idOrderItem;

    public Bill(String dateTime, String dateTimeEnd, String code, int tableId, int userId, int quantity, float totalPrice, int productId, String nameProduct, String nameTable, float priceProduct, int idOrderItem) {
        this.dateTime = dateTime;
        this.dateTimeEnd = dateTimeEnd;
        this.code = code;
        this.tableId = tableId;
        this.userId = userId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productId = productId;
        this.nameProduct = nameProduct;
        this.nameTable = nameTable;
        this.priceProduct = priceProduct;
        this.idOrderItem = idOrderItem;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(String dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public float getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(float priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(int idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "dateTime='" + dateTime + '\'' +
                ", dateTimeEnd='" + dateTimeEnd + '\'' +
                ", code='" + code + '\'' +
                ", tableId=" + tableId +
                ", userId=" + userId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", productId=" + productId +
                ", nameProduct='" + nameProduct + '\'' +
                ", nameTable='" + nameTable + '\'' +
                ", priceProduct=" + priceProduct +
                ", idOrderItem=" + idOrderItem +
                '}';
    }
}
