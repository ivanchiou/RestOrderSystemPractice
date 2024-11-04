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

        setPreferredSize(new Dimension(150, PADDING * 6));
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
        
        // 計算每行可以放置的圖片數量
        int panelWidth = getWidth();
        int imagesPerRow = (panelWidth - PADDING) / (IMAGE_WIDTH + PADDING);
        if (imagesPerRow < 1) imagesPerRow = 1;
        
        int count = 0;
        for (Map.Entry<MenuItem, Image> entry: itemsImage.entrySet()) {
            Image img = entry.getValue();
            if (img != null) {
                // 繪製圖片
                g2d.drawImage(img, x, y, this);

                // 更新位置
                x += IMAGE_WIDTH + PADDING;
                count++;
                
                // 如果達到每行的最大數量，換行
                if (count % imagesPerRow == 0) {
                    x = PADDING;
                    y += IMAGE_HEIGHT + PADDING + 20; // 20是文字的空間
                }
            }
        }
    }
}
