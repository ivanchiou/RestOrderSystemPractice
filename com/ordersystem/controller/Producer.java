package com.ordersystem.controller;

import java.util.concurrent.BlockingQueue;
import com.ordersystem.model.Order;

public class Producer {
    private BlockingQueue<Order> queue;
    private double totalAmount = 0;

    public Producer(BlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Order addOrder() {
        long id = System.currentTimeMillis();
        System.out.println("order id:" + id);
        Order order = new Order(id);
        queue.offer(order);
        return order;
    }

    public BlockingQueue<Order> getQueue() {
        return this.queue;
    }
}
