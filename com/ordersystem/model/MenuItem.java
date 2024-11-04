package com.ordersystem.model;

public class MenuItem implements IMenuItem {
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

    @Override
    public String getImageFilePath() {
        return IMAGE_PATH + this.food.getFileName();
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("%s-%.1f", getName(), getPrice());
    }
}
