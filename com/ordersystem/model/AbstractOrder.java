package com.ordersystem.model;
import java.util.Map;
import java.util.HashMap;

public class AbstractOrder {
    protected long id;
    protected OrderStatus status;
    protected Map<MenuItem, Integer> items;

    public AbstractOrder(long id) {
        this.id = id;
        this.items = new HashMap<>();
        this.status = OrderStatus.PENDING;
    }
}
