import javax.swing.SwingUtilities;

import com.ordersystem.model.Order;
import com.ordersystem.view.OrderSystemUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderSystemUI orderSystemUI = new OrderSystemUI();
                orderSystemUI.setVisible(true);
            }
        });

        System.out.println("Hello, World!");
    }
}