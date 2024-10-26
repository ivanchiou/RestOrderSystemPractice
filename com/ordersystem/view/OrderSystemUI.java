package com.ordersystem.view;
import javax.swing.*;

import com.ordersystem.controller.Producer;
import com.ordersystem.model.Order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

public class OrderSystemUI extends JFrame {
    public OrderSystemUI(Producer producer) {
        ZButton orderButton = new ZButton("Order");
        setSize(800, 600);
        setTitle("點餐系統");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel orderPanel = new JPanel();
        JLabel orderLabel = new JLabel("點餐系統");
        JPanel titlePanel = new JPanel();

        orderPanel.setBackground(java.awt.Color.GREEN);
        orderPanel.add(orderButton);

        titlePanel.add(orderLabel);

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                producer.addOrder();
                System.out.println("Order button clicked!");

                BlockingQueue<Order> queue = producer.getQueue();
                System.out.print("[");
                for (Order order : queue) {
                    System.out.print(order.getId()+",");
                }
                System.out.println("]");
            }
        });

        add(titlePanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.SOUTH);
    }
}
