package com.example.kiotviet_fake.session;

import com.example.kiotviet_fake.models.Product;

import java.util.ArrayList;

public class SessionProducts {
    private static SessionProducts instance;
    private ArrayList<Product> products;

    private SessionProducts() {
        products = new ArrayList<>();
    }

    public static synchronized SessionProducts getInstance() {
        if (instance == null) {
            instance = new SessionProducts();
        }
        return instance;
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public ArrayList<Product> getProductAll() {
        return products;
    }
    public void removeProductAll() {
        products.clear();
    }
}
