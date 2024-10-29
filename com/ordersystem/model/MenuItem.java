package com.ordersystem.model;

public class MenuItem {
    private Food food;
    private String description;

    public MenuItem(Food food, String description) {
        this.food = food;
        this.description = description;
    }

    public String getName() {
        return this.food.getName();
    }

    public double getPrice() {
        return this.food.getPrice();
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("%s-%.1f", getName(), getPrice());
    }
}
