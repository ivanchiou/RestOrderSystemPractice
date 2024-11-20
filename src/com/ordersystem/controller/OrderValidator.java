package com.ordersystem.controller;
import java.util.regex.Pattern;
public class OrderValidator {
    
    // 訂單ID格式驗證：例如 17303803822827880
    private static final Pattern ORDER_ID_PATTERN = 
        Pattern.compile("^\\d{17}$");

    // 價格格式驗證：允許正數，最多兩位小數
    private static final Pattern PRICE_PATTERN = 
        Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    

    // 驗證輸入的數量是否合法（1-99）
    private static final Pattern QUANTITY_PATTERN = 
        Pattern.compile("^[1-9][0-9]?$");

    
    // 驗證訂單ID格式
    public static boolean isValidOrderId(String orderId) {
        if (orderId == null) {
            return false;
        }
        return ORDER_ID_PATTERN.matcher(orderId).matches();
    }

    // 驗證價格格式
    public static boolean isValidPrice(String price) {
        if (price == null) {
            return false;
        }
        return PRICE_PATTERN.matcher(price).matches();
    }

    // 驗證數量格式
    public static boolean isValidQuantity(String quantity) {
        if (quantity == null) {
            return false;
        }
        return QUANTITY_PATTERN.matcher(quantity).matches();
    }
}