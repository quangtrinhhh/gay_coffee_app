package com.example.kiotviet_fake.session;

import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.models.TachDon;

import java.util.ArrayList;

public class SessionManager {
    private static SessionManager instance;
    private ArrayList<Order> orders;
    private ArrayList<Bill> bills;
    private ArrayList<TachDon> tachDons;

    private SessionManager() {
        orders = new ArrayList<>();
        bills = new ArrayList<>();
        tachDons = new ArrayList<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public void addTachDon(TachDon tachDon) {
        tachDons.add(tachDon);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public ArrayList<TachDon> getTachDon() {
        return tachDons;
    }


    public void removeOrderByProductId(String idProductItem) {
        for (Order order : orders) {
            if (order.getIdProductItem().equals(idProductItem)) {
                orders.remove(order);
                break;
            }
        }
    }

    public void removeTachDonByProductId(int idProductItem) {
        for (TachDon tachDon : tachDons) {
            if (tachDon.getIdOrderItem() == idProductItem) {
                tachDons.remove(tachDon);
                break;
            }
        }
    }

    public int getOrderQuantityByIdProductItem(String idProductItem) {
        int quantity = 0;
        for (Order order : orders) {
            if (order.getIdProductItem().equals(idProductItem)) {
                quantity += order.getQuantity();
            }
        }
        return quantity;
    }

    public void updateQuantityProduct(String idProductItem, int newQuantity) {
        for (Order order : orders) {
            if (order.getIdProductItem().equals(idProductItem)) {
                order.setQuantity(newQuantity);
                break;
            }
        }
    }

    public void updateQuantityProductTachDon(int idProductItem, int newQuantity) {
        for (TachDon tachDon : tachDons) {
            if (tachDon.getIdOrderItem() == idProductItem) {
                tachDon.setNewQuantity(newQuantity);
                break;
            }
        }
    }

    public void updateTotalPriceProductTachDon(int idProductItem, int newQuantity) {
        for (TachDon tachDon : tachDons) {
            if (tachDon.getIdOrderItem() == idProductItem) {
                tachDon.setTotalPrice(tachDon.getPriceProduct() * newQuantity);
                break;
            }
        }
    }

    public void removeOrderAll() {
        orders.clear();
    }

    public void removeBillAll() {
        bills.clear();
    }

    public void removeTachDonAll() {
        tachDons.clear();
    }

}