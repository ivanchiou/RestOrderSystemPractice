import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import com.ordersystem.controller.Consumer;
import com.ordersystem.controller.Producer;
import com.ordersystem.model.Order;
import com.ordersystem.view.OrderSystemUI;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<Order>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderSystemUI orderSystemUI = new OrderSystemUI(producer, consumer);
                orderSystemUI.setVisible(true);
            }
        });

        System.out.println("Hello, World!");
    }
}