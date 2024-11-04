package com.ordersystem.controller;
import com.ordersystem.model.Order;
import com.ordersystem.model.OrderStatus;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Food;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final String STRING_MENU_ITEM = "Menu Items:";
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
                writer.print(STRING_MENU_ITEM);
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
        if (orderFiles.length == 0) {
            return orders;
        }
        Arrays.sort(orderFiles, (f1, f2) -> { return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());});
        File orderFile = orderFiles[0];
        lock.readLock().lock();
        try(BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            Order newOrder = null;
            while((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID:")) {
                    long orderId = Long.parseLong(line.substring("Order ID:".length()).trim());
                    newOrder = new Order(orderId);
                    orders.add(newOrder);
                } else if (line.startsWith("Order Status:")) {
                    if (newOrder != null) {
                        newOrder.setOrderStatus(OrderStatus.valueOf(line.substring("Order Status:".length()).trim()));
                    }
                } else if (line.startsWith("Order Time:")) {

                } else if (line.startsWith(STRING_MENU_ITEM)) {
                    if (newOrder != null) {
                        String itemDetail = line.substring(STRING_MENU_ITEM.length()).trim();
                        // format: 咖啡-40.0-1
                        String[] parts = itemDetail.split("-");
                        // parts[0] = "咖啡", parts[1] = "40.0", parts[2] = "1"
                        Food[] foods = Food.values();
                        for (Food food : foods) {
                            if (food.getName().equals(parts[0])) {
                                MenuItem item = new MenuItem(food, "");
                                newOrder.setItem(item, Integer.parseInt(parts[2]));
                                break;
                            }
                        }
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
