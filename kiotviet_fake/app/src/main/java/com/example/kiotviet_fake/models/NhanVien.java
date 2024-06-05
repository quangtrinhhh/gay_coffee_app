package com.example.kiotviet_fake.models;

public class NhanVien {
    private int id;
    private String name;
    private String password;
    private String role;
    private int idShop;

    public NhanVien(int id, String name, String password, String role, int idShop) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.idShop = idShop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
