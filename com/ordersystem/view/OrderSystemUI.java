package com.ordersystem.view;
import javax.swing.*;

import com.ordersystem.controller.Producer;
import com.ordersystem.controller.Consumer;
import com.ordersystem.model.Order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

public class OrderSystemUI extends JFrame {
    private Thread consumerThread;
    public OrderSystemUI(Producer producer, Consumer consumer) {
        consumerThread = new Thread((Runnable)consumer);
        ZButton orderButton = new ZButton("Produce Order");
        ZButton consumeButton = new ZButton("Consume Order");
        setSize(800, 600);
        setTitle("點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        JLabel orderLabel = new JLabel("點餐系統");
        JPanel titlePanel = new JPanel();

        controlPanel.setBackground(java.awt.Color.GREEN);
        controlPanel.add(orderButton);
        controlPanel.add(consumeButton);

        titlePanel.add(orderLabel);

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = producer.addOrder();
                System.out.println("Order button clicked!" + order.getId()); // 顯示現在訂單的ID

                // 目前所有已經送到BlockingQueue(餐廳後台)的訂單
                BlockingQueue<Order> queue = producer.getQueue();
                System.out.print("[");
                for (Order o : queue) {
                    System.out.print(o.getId()+",");
                }
                System.out.println("]");
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

        add(titlePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }
}
