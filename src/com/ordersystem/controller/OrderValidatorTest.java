package com.ordersystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * 訂單驗證器測試類
 * 測試訂單ID、價格和數量的驗證邏輯
 */
@DisplayName("訂單驗證器測試")
public class OrderValidatorTest {
    
    @DisplayName("測試有效的訂單ID")
    @ParameterizedTest(name = "訂單ID {0} 應該是有效的")
    @ValueSource(strings = {
        "17303803822827880",  // 標準17位數ID
        "12345678901234567",  // 另一個有效ID
        "98765432109876543"   // 第三個有效ID
    })
    void shouldValidateCorrectOrderIds(String orderId) {
        assertTrue(OrderValidator.isValidOrderId(orderId), 
                  "訂單ID應該是17位數字");
    }

    @DisplayName("測試無效的訂單ID")
    @ParameterizedTest(name = "訂單ID {0} 應該是無效的")
    @ValueSource(strings = {
        "123",                // 太短
        "123456789012345678", // 太長
        "1234567890123456a",  // 包含字母
        "12.34567890123456"   // 包含小數點
    })
    @NullAndEmptySource      // 測試null和空字串
    void shouldNotValidateIncorrectOrderIds(String orderId) {
        assertFalse(OrderValidator.isValidOrderId(orderId), 
                   "非17位數字的訂單ID應該是無效的");
    }

    @DisplayName("測試有效的價格格式")
    @ParameterizedTest(name = "價格 {0} 應該是有效的")
    @ValueSource(strings = {
        "99.99",    // 兩位小數
        "100",      // 整數
        "0.99",     // 小於1
        "1234.56",  // 大數字
        "0.5",      // 一位小數
        "1000000"   // 大整數
    })
    void shouldValidateCorrectPrices(String price) {
        assertTrue(OrderValidator.isValidPrice(price), 
                  "價格格式應該是有效的");
    }

    @DisplayName("測試無效的價格格式")
    @ParameterizedTest(name = "價格 {0} 應該是無效的")
    @ValueSource(strings = {
        "-99.99",   // 負數
        "abc",      // 非數字
        "99.999",   // 超過兩位小數
        "12..34",   // 多個小數點
        ".99",      // 缺少整數部分
        "99.",      // 小數點後無數字
        "1,234.56"  // 包含分隔符
    })
    @NullAndEmptySource
    void shouldNotValidateIncorrectPrices(String price) {
        assertFalse(OrderValidator.isValidPrice(price), 
                   "無效的價格格式應該被拒絕");
    }

    @DisplayName("測試有效的數量格式")
    @ParameterizedTest(name = "數量 {0} 應該是有效的")
    @ValueSource(strings = {
        "1",        // 最小有效值
        "10",       // 兩位數
        "99",      // 最大數
    })
    void shouldValidateCorrectQuantities(String quantity) {
        assertTrue(OrderValidator.isValidQuantity(quantity),
                  "數量應該是正整數");
    }

    @DisplayName("測試無效的數量格式")
    @ParameterizedTest(name = "數量 {0} 應該是無效的")
    @ValueSource(strings = {
        "0",        // 零
        "-1",       // 負數
        "1.5",      // 小數
        "abc",      // 非數字
        "01",        // 前導零
        "100"      // 超過最大數量
    })
    @NullAndEmptySource
    void shouldNotValidateIncorrectQuantities(String quantity) {
        assertFalse(OrderValidator.isValidQuantity(quantity),
                   "無效的數量格式應該被拒絕");
    }

    @Test
    @DisplayName("測試極限情況的訂單ID")
    void testEdgeCasesForOrderId() {
        String maxLongAsString = String.valueOf(Long.MAX_VALUE);
        assertFalse(OrderValidator.isValidOrderId(maxLongAsString),
                   "超過17位的數字應該是無效的");
    }

    @Test
    @DisplayName("測試極限情況的價格")
    void testEdgeCasesForPrice() {
        String maxDoubleAsString = String.valueOf(Double.MAX_VALUE);
        assertFalse(OrderValidator.isValidPrice(maxDoubleAsString),
                   "過大的數字應該是無效的價格格式");
    }
}