#!/bin/bash

# backup_orders.sh
# 訂單資料備份腳本

# 設定變數
BACKUP_DIR="./backup/orders"
SOURCE_DIR="./orders"
DATE=$(date +%Y%m%d)
BACKUP_FILE="orders_backup_$DATE.tar.gz"

# 創建備份目錄
mkdir -p $BACKUP_DIR

# 創建備份
tar -czf $BACKUP_DIR/$BACKUP_FILE $SOURCE_DIR

# 移除7天前的備份
find $BACKUP_DIR -name "orders_backup_*.tar.gz" -mtime +7 -delete

# 輸出備份狀態
if [ $? -eq 0 ]; then
    echo "備份成功完成: $BACKUP_FILE"
else
    echo "備份失敗"
fi