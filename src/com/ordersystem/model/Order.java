package com.ordersystem.model;
import java.util.Map;
import java.time.LocalDateTime;

public class Order extends AbstractOrder implements IOrder {
    private LocalDateTime time;

    public Order(long id) {
        super(id);
    }

    @Override
    public long getId() {
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

    @Override
    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return this.status;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

