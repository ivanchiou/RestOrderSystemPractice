package com.ordersystem.controller;

import java.util.concurrent.BlockingQueue;
import com.ordersystem.model.Order;
import java.util.Random;

public class Producer {
    private BlockingQueue<Order> queue;
    private double totalAmount = 0;
    private final Random random = new Random();

    public Producer(BlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Order addOrder(Order order) {
        queue.offer(order); // non-blocking produce
        return order;
    }

    public BlockingQueue<Order> getQueue() {
        return this.queue;
    }
}
