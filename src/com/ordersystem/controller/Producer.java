package com.ordersystem.controller;
import com.ordersystem.model.AbstractProducer;
import com.ordersystem.model.Order;
import java.util.concurrent.BlockingQueue;

public class Producer extends AbstractProducer {
    private double totalAmount = 0;

    public Producer(BlockingQueue<Order> queue) {
        super(queue);
    }

    @Override
    public double getTotalAmount() {
        return this.totalAmount;
    }
}
