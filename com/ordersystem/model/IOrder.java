package com.ordersystem.model;
import java.util.Map;

public interface IOrder {
    long getId();
    void setItem(MenuItem item, int quantity);
    Map<MenuItem, Integer> getItems();
    Map.Entry<MenuItem, Integer> getItem(String name);
}
