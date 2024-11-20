package com.ordersystem.view;
import com.ordersystem.model.MenuItem;
import java.awt.*;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

class FoodIconPanel extends JPanel {
    private static final int MAX_ICON_SIZE = 90;
    private final Map<MenuItem, Integer> itemsCount = new HashMap<>();
    private final Map<MenuItem, Image> itemsImage = new HashMap<>();

    public FoodIconPanel(MenuItem[] items) {
        setPreferredSize(new Dimension(200, MAX_ICON_SIZE * 6));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // setBackground(UiStyle.SECONDARY_COLOR);
        // setBorder(BorderFactory.createCompoundBorder(
        //         new LineBorder(UiStyle.BORDER_COLOR, 2),
        //         BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        // setFont(UiStyle.getCommonFont());

        for (MenuItem item : items ) {
            this.itemsCount.put(item, 0);
            loadImage(item);
        }
    }

    private void loadImage(MenuItem item) {
        try {
            String imagePath = item.getImageFilePath();
            URL imageUrl = getClass().getResource("/"+imagePath);
            if (imageUrl == null) {
                System.err.println("圖片文件不存在: " + imagePath);
                return;
            }
            Image originalImg =ImageIO.read(imageUrl);
            int originalWidth = originalImg.getWidth(null);
            int originalHeight = originalImg.getHeight(null);

            double scale = Math.min((double) MAX_ICON_SIZE / originalWidth, (double) MAX_ICON_SIZE / originalHeight);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            Image scaledImg = originalImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            this.itemsImage.put(item, scaledImg);
        } catch (IOException e) {
            System.err.println("無法載入圖片: " + item.getImageFilePath());
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int y = 0;

        for (Map.Entry<MenuItem, Integer> entry : itemsCount.entrySet()) {
            Image img = itemsImage.get(entry.getKey());
            if (img != null) {
                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                g2d.drawImage(img, 20 + (MAX_ICON_SIZE - imgWidth) / 2, y, this);
                g2d.setColor(new Color(55, 55, 55));
                String text = String.format("%s: %d ($%.2f)",
                        entry.getKey().getName(),
                        entry.getValue(),
                        entry.getKey().getPrice() * entry.getValue());
                g2d.drawString(text, 20, y + imgHeight + 5);

                y += MAX_ICON_SIZE + 30;
                if (y >= getHeight()) {
                    y = 0;
                }
            }
        }
    }

    public void updateCount(MenuItem item, int quantity) {
        if (item != null) {
            this.itemsCount.put(item, this.itemsCount.get(item) + quantity);
            repaint();
        }
    }
}
