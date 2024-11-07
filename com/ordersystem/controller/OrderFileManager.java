package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ordersystem.model.Food;
import com.ordersystem.model.MenuItem;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
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
                int size = order.getItems().size();
                int count = 0;
                for (Map.Entry<MenuItem, Integer> item : order.getItems().entrySet()) {
                    writer.printf("%s-%.1f-%d", item.getKey().getName(), item.getKey().getPrice(), item.getValue());
                    if (count < size - 1) {
                        writer.printf(",");
                    }
                }
                writer.println("\n========================");
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
        
        // 把orders檔案依照時間排序，最新的在最前面
        Arrays.sort(orderFiles, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));
        // 並取出最新的order檔案，只讀取最新的
        File orderFile = orderFiles[0];
        String line;
        lock.readLock().lock();
        try(BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            Order newOrder = null;
            while((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID:")) {
                    String orderIdString = line.substring("Order ID:".length()).trim();
                    if (!OrderValidator.isValidOrderId(orderIdString)) {
                        throw new IllegalArgumentException("Invalid order id: " + orderIdString);
                    } else {
                        System.out.println("Order ID: " + orderIdString + " is valid.");
                        long orderId = Long.parseLong(orderIdString);
                        newOrder = new Order(orderId);
                        orders.add(newOrder);
                    }
                } else if (line.startsWith("Order Status:")) {
                    newOrder.setOrderStatus(OrderStatus.valueOf(line.substring("Order Status:".length()).trim()));
                } else if (line.startsWith("Order Time:")) {
                    if (newOrder != null) {
                        String timeStr = line.substring("Order Time:".length()).trim();
                        LocalDateTime orderTime = timeStr.equals("null") ? null : LocalDateTime.parse(timeStr);
                        newOrder.setTime(orderTime);
                    }
                } else if (line.startsWith("Menu Items:")) {
                    if (newOrder != null) {
                        // Format: "Menu Items:漢堡-50.0-1"
                        String itemDetail = line.substring("Menu Items:".length()).trim();
                        String[] parts = itemDetail.split("-");
                        String itemName = parts[0].trim();
                        Food[] foods = Food.values();
                        Food itemFood = null;
                        for (Food food : foods) {
                            if (!OrderValidator.isValidPrice(parts[1])) {
                                throw new IllegalArgumentException("Invalid price: " + parts[1]);
                            }
                            else if (!OrderValidator.isValidQuantity(parts[2])) {
                                throw new IllegalArgumentException("Invalid quantity: " + parts[2]);
                            }
                            else if (food.getName().equals(itemName)) {
                                itemFood = food;
                                break;
                            }
                        }
                        int quantity = Integer.parseInt(parts[2].trim());                      
                        MenuItem menuItem = new MenuItem(itemFood, "");
                        newOrder.setItem(menuItem, quantity);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }

        return orders;
    }
}
