package com.ordersystem.view;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderSystemUI extends JFrame {
    public OrderSystemUI() {
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
                
                System.out.println("Order button clicked!");
            }
        });

        add(titlePanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.SOUTH);
    }
}
