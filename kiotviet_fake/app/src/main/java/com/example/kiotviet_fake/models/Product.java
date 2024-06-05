package com.example.kiotviet_fake.models;

public class Product {
    public int id;
    public String idProductItem;
    public String name;
    public String price;
    public int quantity;
    public int quantityOrder;
    public int idTable;
    public String nameTable;
    public int idProduct;
    public String nameCategories;
    public String product_code;
    public int idCategories;

    public Product(int id, String idProductItem, String name, String price, int quantity, int quantityOrder, int idTable, String nameTable, int idProduct, String nameCategories, String product_code, int idCategories) {
        this.id = id;
        this.idProductItem = idProductItem;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.quantityOrder = quantityOrder;
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.idProduct = idProduct;
        this.nameCategories = nameCategories;
        this.product_code = product_code;
        this.idCategories = idCategories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProductItem() {
        return idProductItem;
    }

    public void setIdProductItem(String idProductItem) {
        this.idProductItem = idProductItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameCategories() {
        return nameCategories;
    }

    public void setNameCategories(String nameCategories) {
        this.nameCategories = nameCategories;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }
}