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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import com.ordersystem.controller.OrderFileManager;

public class OrderSystemUI extends JFrame {
    private Thread consumerThread;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final OrderFileManager orderFileManager = new OrderFileManager();

    public OrderSystemUI(Producer producer, Consumer consumer) {
        //readOrders()

        consumerThread = new Thread((Runnable)consumer);

        // 把所有的食物放到菜單
        Food[] foods = Food.values(); 
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
                    String printString = "目前後台等待的訂單:"+concatOrdersToString(queue);
                    printString += "\n已經處理的訂單:"+concatOrdersToString(consumer.getDeliveredOrders());
                    orderTextArea.setText(printString);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        queuemonitor.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window Close");
                // saveOrders
                orderFileManager.saveOrders(consumer.getDeliveredOrders());
                dispose();
            }
        });

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(orderTextArea), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private String concatOrdersToString(Collection<Order> orders) {
        String printString = "[";
        int size = orders.size();
        int count = 0;
        for (Order o : orders) {
            printString += o.getId()+ ":" + o.getItems();
            if (count < size - 1) {
                printString += ",";
            }
            count++;
        }
        printString += "]";

        return printString;
    }
}
