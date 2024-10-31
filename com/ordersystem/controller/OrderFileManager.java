package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.ordersystem.model.MenuItem;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;

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

    public void saveOrders(Collection<Order> orders) {
        String filename = "orders-" + LocalDateTime.now().format(DATE_FORMATTER) + ".txt";
        File file = new File(BASE_DIRECTORY, filename);

        lock.writeLock().lock();
        try(PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            for (Order order : orders) {
                writer.println("==== Orders Details ====");
                writer.println("Order ID:" + order.getId());
                writer.println("Order Time:"+ order.getTime());
                writer.println("Order Status:" + order.getOrderStatus());
                writer.print("Menu Items:");
                for (Map.Entry<MenuItem, Integer> item : order.getItems().entrySet()) {
                    writer.printf("%s-%.1f-%d\n", item.getKey().getName(), item.getKey().getPrice(), item.getValue());
                }
                writer.println("========================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Order> readOrders() {
        List<Order> orders = new ArrayList<>();
        File directory = new File(BASE_DIRECTORY);
        File[] orderFiles = directory.listFiles();
        String line;
        for (File orderFile : orderFiles) {
            lock.readLock().lock();
            try(BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
                Order newOrder = null;
                while((line = reader.readLine()) != null) {
                    if (line.startsWith("Order ID:")) {
                        long orderId = Long.parseLong(line.substring("Order ID:".length()).trim());
                        newOrder = new Order(orderId);
                        orders.add(newOrder);
                    } else if (line.startsWith("Order Status:")) {
                        newOrder.setOrderStatus(OrderStatus.valueOf(line.substring("Order Status:".length()).trim()));
                    } else if (line.startsWith("Order Time:")) {

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.readLock().unlock();
            }
        }

        return orders;
    }
}
