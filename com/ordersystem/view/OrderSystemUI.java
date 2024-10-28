package com.ordersystem.view;
import javax.swing.*;

import com.ordersystem.controller.Producer;
import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.OrderFactory;
import com.ordersystem.model.Order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

public class OrderSystemUI extends JFrame {
    private Thread consumerThread;
    public OrderSystemUI(Producer producer, Consumer consumer) {
        consumerThread = new Thread((Runnable)consumer);
 
        setSize(800, 600);
        setTitle("點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel orderLabel = new JLabel("點餐系統");
        JPanel titlePanel = new JPanel();
        titlePanel.add(orderLabel);
        
        JPanel controlPanel = new JPanel();
        ZButton orderButton = new ZButton("Produce Order");
        ZButton consumeButton = new ZButton("Consume Order");

        controlPanel.setBackground(java.awt.Color.GREEN);
        controlPanel.add(orderButton);
        controlPanel.add(consumeButton);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = OrderFactory.createNextOrder();
                producer.addOrder(order);
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
                    System.out.print("目前後台訂單:[");
                    for (Order o : queue) {
                        System.out.print(o.getId()+",");
                    }
                    System.out.println("]");
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        queuemonitor.start();

        add(titlePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }
}
