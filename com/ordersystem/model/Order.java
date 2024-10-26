package com.ordersystem.model;
import java.util.Map;
import java.time.LocalDateTime;

enum OrderStatus {
    PENDING, PROCESSING, COMPLETED
}

public class Order extends AbstractOrder implements IOrder {
    private LocalDateTime time;

    public Order(int id) {
        super(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setItem(MenuItem item, int quantity) {
        items.put(item, quantity);
    }

    @Override
    public Map<MenuItem, Integer> getItems() {
        return this.items;
    }

    @Override
    public Map.Entry<MenuItem, Integer> getItem(String name) {
        for ( Map.Entry<MenuItem, Integer> entry : this.items.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry;
            }
        }

        return null;
    }
}

