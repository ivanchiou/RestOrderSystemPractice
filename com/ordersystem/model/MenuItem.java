package com.ordersystem.model;

public class MenuItem {
    private String name;
    private double price;
    private String description;
    private OrderSet set;

    public MenuItem(String name, double price, String description, OrderSet set) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.set = set;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }
}
