package com.ordersystem.view;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import com.ordersystem.model.MenuItem;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.util.Map;
import java.util.HashMap;
public class FoodIconPanel extends JPanel {
    private final Map<MenuItem, Image> itemsImage = new HashMap<>();
    private static final int IMAGE_WIDTH = 100;   // 設定固定的圖片寬度
    private static final int IMAGE_HEIGHT = 100;  // 設定固定的圖片高度
    private static final int PADDING = 10;        // 圖片之間的間距
    public FoodIconPanel(MenuItem[] items) {
        setPreferredSize(new Dimension(IMAGE_WIDTH + PADDING * 2, PADDING * 6));

        for (MenuItem item: items) {
            try {
                File imageFile = new File(item.getImageFilePath());

                if (!imageFile.exists()) {
                    System.err.println("圖片不存在");
                    return;
                }

                // 讀取並調整圖片大小
                Image originalImage = ImageIO.read(imageFile);
                Image scaledImage = originalImage.getScaledInstance(
                    IMAGE_WIDTH, 
                    IMAGE_HEIGHT, 
                    Image.SCALE_SMOOTH
                );

                this.itemsImage.put(item, scaledImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int x = PADDING;  // 起始X座標
        int y = PADDING;  // 起始Y座標
        for (Map.Entry<MenuItem, Image> entry: itemsImage.entrySet()) {
            Image img = entry.getValue();
            if (img != null) {
                // 繪製圖片
                g2d.drawImage(img, x, y, this);
                
                // 每一個food增加的y的距離
                y += IMAGE_HEIGHT + PADDING;
            }
        }
    }
}
