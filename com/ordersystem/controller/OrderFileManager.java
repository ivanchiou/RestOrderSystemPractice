package com.ordersystem.controller;
import com.ordersystem.model.Order;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;  
import java.util.List;
import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class OrderFileManager {
    private final String BASE_DIRECTORY = "orders";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public OrderFileManager() {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveOrders(List<Order> orders) {
        String filename = "orders-" + LocalDateTime.now().format(DATE_FORMATTER) + ".txt";
        File file = new File(BASE_DIRECTORY, filename);

        lock.writeLock().lock();
        try(PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            for (Order order : orders) {
                writer.println("==== Orders Details ====");
                writer.println("Order ID:" + order.getId());
                writer.println("Order Status:" + order.getOrderStatus());
                writer.println("========================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Order> readOrders() {
        return null;
    }
}
