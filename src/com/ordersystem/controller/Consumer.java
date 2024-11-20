package com.ordersystem.controller;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;

public class Consumer implements Runnable {
    private BlockingQueue<Order> queue;
    private final List<Order> deliveredList = new ArrayList<>();
    private final Random random = new Random();
    private final int totalAmount = 0;

    public Consumer(BlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = queue.take(); // blocking
                processOrder(order);
                deliveredOrder(order);
                this.deliveredList.add(order);
                Thread.sleep(random.nextInt(2000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processOrder(Order order) {
        order.setOrderStatus(OrderStatus.PROCESSING);
        System.out.println("處裡訂單中..." + order.getId());
    }

    private void deliveredOrder(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETED);
        System.out.println("餐點已送出:" + order.getId());
    }

    public int getDeliveredOrderNumber() {
        return this.deliveredList.size();
    }

    public List<Order> getDeliveredOrders() {
        return this.deliveredList;
    }

    public void addDeliveredOrders(Order order) {
        this.deliveredList.add(order);
    }
}
