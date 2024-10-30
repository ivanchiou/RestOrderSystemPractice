# 點餐系統

基於 Producer-Consumer 模式的多執行緒點餐系統，用於模擬餐廳點餐流程。

## 目錄
- [系統概述](#系統概述)
- [功能需求](#功能需求)
- [非功能需求](#非功能需求)
- [系統架構](#系統架構)
- [技術要求](#技術要求)

## 系統概述

本系統是一個基於 Producer-Consumer 模式的多執行緒點餐系統，具備以下特點：
- 模擬實際餐廳點餐流程
- 包含客戶下單、訂單處理和完成功能
- 提供圖形用戶界面(GUI)
- 支持客戶自助下單
- 提供餐廳員工訂單處理功能

## 功能需求

### 客戶界面 (View)
- 食品選擇下拉菜單
- 食品數量選擇功能
- 送出訂單按鈕
- 訂單狀態顯示
  - 已生產訂單數
  - 已消費訂單數
  - 等待中訂單數
- 食品圖標和訂單數量顯示

### 員工界面 (View)
- 訂單處理系統控制
  - 開始按鈕
  - 停止按鈕
- 訂單處理狀態顯示

### 訂單處理 (Controller)
- Producer-Consumer 設計模式實現
  - Producer: 接收和排隊新訂單
  - Consumer: 處理訂單

### 數據模型 (Model)
訂單資料包含：
- ID
- 食品項目
- 數量
- 價格
- 狀態（待處理、處理中、已完成）

## 非功能需求

### 性能
- 支持多訂單並行處理
- GUI 保持響應性

### 可靠性
- 具備異常輸入處理能力
- 系統穩定性保證

### 可擴展性
- 支持新增食品項目
- 架構支持功能擴展

### 用戶體驗
- 直觀的 GUI 設計
- 保持食品圖標原始比例

## 系統架構

### 模型 (Model)
- `IOrder`: 訂單接口
- `Order`: 具體訂單類
- `OrderStatus`: 訂單狀態枚舉

### 視圖 (View)
- `OrderingSystemGUI`: 主 GUI 類
- `IconPanel`: 食品圖標顯示面板

### 控制器 (Controller)
- `IProducer`: 生產者接口
- `IConsumer`: 消費者接口
- `Producer`: 具體生產者類
- `Consumer`: 具體消費者類

### 工具 (Util)
- `FoodIcon`: 食品圖標枚舉類

## 技術要求

- 開發語言：Java
  - 物件導向設計
  - 抽象類別
  - 介面實現
- 多執行緒程式設計
- GUI 框架：Swing
