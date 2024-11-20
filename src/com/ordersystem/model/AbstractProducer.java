package com.ordersystem.model;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractProducer implements IProducer {
    private BlockingQueue<Order> queue;

    public AbstractProducer(BlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void addOrder(Order order) {
        queue.offer(order); // non-blocking produce
    }

    public BlockingQueue<Order> getQueue() {
        return this.queue;
    }

    public abstract double getTotalAmount();
}