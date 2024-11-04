package com.ordersystem.model;
import java.util.concurrent.BlockingQueue;

public interface IProducer {
    Order addOrder(Order order);
    BlockingQueue<Order> getQueue();
}
