package com.ordersystem.view;
import javax.swing.*;

import com.ordersystem.controller.Producer;
import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.OrderFactory;
import com.ordersystem.model.MenuItem;
import com.ordersystem.model.Order;
import com.ordersystem.model.Food;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.ArrayList;

public class OrderSystemUI extends JFrame {
    private Thread consumerThread;
    private final List<MenuItem> menuItems = new ArrayList<>();

    // private MenuItem[] generateMenuItems() {
    //     Food[] foods = Food.class.getEnumConstants();

    //     // 把所有食物加到MenuItems
    //     MenuItem[] items = new MenuItem[foods.length];
    //     for (int i = 0 ; i< foods.length ; i++) {
    //         items[i] = new MenuItem(foods[i], "");
    //     }

    //     return items;
    // }

    public OrderSystemUI(Producer producer, Consumer consumer) {
        consumerThread = new Thread((Runnable)consumer);

        Food[] foods = Food.class.getEnumConstants();
        for(Food food : foods) {
            menuItems.add(new MenuItem(food, ""));
        }

        setSize(800, 600);
        setTitle("點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel orderLabel = new JLabel("點餐系統");
        JPanel titlePanel = new JPanel();
        titlePanel.add(orderLabel);

        JTextArea orderTextArea = new JTextArea();
        
        JPanel controlPanel = new JPanel();
        ZButton orderButton = new ZButton("Produce Order");
        ZButton consumeButton = new ZButton("Consume Order");
        JComboBox<MenuItem> itemComboBox = new JComboBox<>(menuItems.toArray(new MenuItem[0]));
        
        controlPanel.setBackground(java.awt.Color.GREEN);
        controlPanel.add(itemComboBox);
        controlPanel.add(orderButton);
        controlPanel.add(consumeButton);

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = OrderFactory.createNextOrder((MenuItem)itemComboBox.getSelectedItem());
                producer.addOrder(order); // 把order加到BlockingQueue裡面
                System.out.println("Order button clicked!" + order.getId()); // 顯示現在訂單的ID
            }
        });

        consumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Consume Order")) {
                    System.out.println("Consumer starts to work...");
                    consumerThread.start();
                    consumeButton.setEnabled(false);
                }
            }
        });

        Thread queuemonitor = new Thread(() -> {
            // 目前所有已經送到BlockingQueue(餐廳後台)的訂單
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    BlockingQueue<Order> queue = producer.getQueue();
                    String printString = "目前後台等待的訂單:[";
                    int size = queue.size();
                    int count = 0;
                    for (Order o : queue) {
                        printString += o.getId()+ ":" + o.getItems();
                        if (count < size - 1) {
                            printString += ",";
                        }
                        count++;
                    }
                    printString += "]\n";

                    printString += "已經處理的訂單:[";
                    size = consumer.getDeliveredOrders().size();
                    count = 0;
                    for (Order o : consumer.getDeliveredOrders()) {
                        printString += o.getId()+ ":" + o.getItems() + ":" + o.getOrderStatus();
                        if (count < size - 1) {
                            printString += ",";
                        }
                        count++;
                    }
                    printString += "]\n";

                    orderTextArea.setText(printString);
                    //System.out.println(printString);

                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        queuemonitor.start();

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(orderTextArea), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    // private String concatOrdersToString(Order[] orders) {
    //     String printString = "目前後台等待的訂單:[";
    //     int size = orders.length;
    //     int count = 0;
    //     for (Order o : orders) {
    //         printString += o.getId()+ ":" + o.getItems();
    //         if (count < size - 1) {
    //             printString += ",";
    //         }
    //         count++;
    //     }
    //     printString += "]\n";

    //     return printString;
    // }
}
