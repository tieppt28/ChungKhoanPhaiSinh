
## 📊 Output mẫu

### Menu chính
```
=== HỆ THỐNG DỰ ĐOÁN XU HƯỚNG CHỨNG KHOÁN ===
=== MENU CHÍNH ===
1. Phân tích dữ liệu ngẫu nhiên
2. Phân tích dữ liệu theo mẫu  
3. Hiển thị chỉ báo kỹ thuật
4. Hiển thị quy tắc dự đoán
0. Thoát
```

### Phân tích dữ liệu
```
=== PHÂN TÍCH DỮ LIỆU NGẪU NHIÊN ===
Đã tạo 100 điểm dữ liệu
Giá bắt đầu: 100.00 VND
Giá hiện tại: 105.23 VND

--- TÌNH HÌNH THỊ TRƯỜNG ---
XU HƯỚNG TĂNG (EMA 20: 104.56 > EMA 50: 102.34, chênh lệch: +2.17%). 
ĐỘNG LỰC TÍCH CỰC (RSI: 58.3) - Thị trường khỏe mạnh

--- TÍN HIỆU GIAO DỊCH ---
Tìm thấy 2 tín hiệu:
• PredictionSignal{timestamp=2024-01-15T10:30, signal=LONG, confidence=78.5%, reason='TÍN HIỆU MUA: EMA 20 (104.56) cắt lên EMA 50 (102.34), RSI: 58.3 > 55. Xác nhận bởi MACD cắt lên.', price=105.23}
```

### TestDataRunner Output
```
=== CHẠY TEST DỮ LIỆU DỰ ĐOÁN TOÀN DIỆN ===

1. TEST XU HƯỚNG TĂNG MẠNH:
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: BULL (80 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 100.00 → 110.25 VND ( +10.25% - TĂNG)
EMA 20: 109.87 VND
EMA 50: 105.43 VND
EMA 20 > EMA 50: Xu hướng tăng
RSI: 62.1 (TÍCH CỰC - Động lực tốt)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG TĂNG (EMA 20: 109.87 > EMA 50: 105.43, chênh lệch: +4.21%). 
ĐỘNG LỰC TÍCH CỰC (RSI: 62.1) - Thị trường khỏe mạnh
TÍN HIỆU: MUA (LONG) (Độ tin cậy: 82.3%)
LÝ DO: TÍN HIỆU MUA: EMA 20 (109.87) cắt lên EMA 50 (105.43), RSI: 62.1 > 55. Xác nhận bởi MACD cắt lên.
KHUYẾN NGHỊ: Mua vào với độ tin cậy CAO. Đặt stop-loss dưới mức hỗ trợ gần nhất.
═══════════════════════════════════════════════════════

2. TEST XU HƯỚNG GIẢM MẠNH:
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: BEAR (80 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 150.00 → 135.50 VND ( -9.67% - GIẢM)
EMA 20: 133.45 VND
EMA 50: 138.92 VND
EMA 20 < EMA 50: Xu hướng giảm
RSI: 38.2 (TRUNG TÍNH - Chờ tín hiệu)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG GIẢM (EMA 20: 133.45 < EMA 50: 138.92, chênh lệch: -3.94%). 
TRUNG TÍNH (RSI: 38.2) - Chờ tín hiệu rõ ràng hơn
TÍN HIỆU: BÁN (SHORT) (Độ tin cậy: 71.8%)
LÝ DO: TÍN HIỆU BÁN: EMA 20 (133.45) cắt xuống EMA 50 (138.92), RSI: 38.2 < 45. Xác nhận bởi MACD cắt xuống.
KHUYẾN NGHỊ: Bán ra với độ tin cậy CAO. Đặt stop-loss trên mức kháng cự gần nhất.
═══════════════════════════════════════════════════════

3. TEST THỊ TRƯỜNG ĐI NGANG:
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: SIDE (80 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 120.00 → 121.50 VND ( +1.25% - TĂNG)
EMA 20: 120.45 VND
EMA 50: 120.12 VND
EMA 20 > EMA 50: Xu hướng tăng
RSI: 48.7 (TRUNG TÍNH - Chờ tín hiệu)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG TĂNG (EMA 20: 120.45 > EMA 50: 120.12, chênh lệch: +0.27%). 
TRUNG TÍNH (RSI: 48.7) - Chờ tín hiệu rõ ràng hơn
TÍN HIỆU: HOLD (GIỮ) - Không có tín hiệu giao dịch rõ ràng
KHUYẾN NGHỊ: Tiếp tục theo dõi và chờ tín hiệu rõ ràng hơn
═══════════════════════════════════════════════════════

5. TEST ĐIỀU KIỆN QUÁ MUA/QUÁ BÁN:
   a) Điều kiện quá mua (RSI > 70):
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: OVER (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 100.00 → 125.67 VND ( +25.67% - TĂNG)
EMA 20: 124.23 VND
EMA 50: 115.45 VND
EMA 20 > EMA 50: Xu hướng tăng
RSI: 78.5 (QUÁ MUA - Cảnh báo điều chỉnh)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG TĂNG (EMA 20: 124.23 > EMA 50: 115.45, chênh lệch: +7.61%). 
QUÁ MUA (RSI: 78.5) - Cần cảnh giác với điều chỉnh
TÍN HIỆU: ĐẢO CHIỀU (REVERSAL) (Độ tin cậy: 68.4%)
LÝ DO: CẢNH BÁO QUÁ MUA: RSI = 78.5 > 70. Thị trường đang trong vùng quá mua, có khả năng cao sẽ có điều chỉnh giảm.
KHUYẾN NGHỊ: Chuẩn bị cho sự đảo chiều với độ tin cậy CAO. Cân nhắc chốt lời hoặc cắt lỗ.
═══════════════════════════════════════════════════════

   b) Điều kiện quá bán (RSI < 30):
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: UNDER (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 100.00 → 78.45 VND ( -21.55% - GIẢM)
EMA 20: 79.12 VND
EMA 50: 85.67 VND
EMA 20 < EMA 50: Xu hướng giảm
RSI: 22.3 (QUÁ BÁN - Cơ hội mua vào)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG GIẢM (EMA 20: 79.12 < EMA 50: 85.67, chênh lệch: -7.65%). 
QUÁ BÁN (RSI: 22.3) - Cơ hội mua vào tiềm năng
TÍN HIỆU: ĐẢO CHIỀU (REVERSAL) (Độ tin cậy: 75.6%)
LÝ DO: CƠ HỘI MUA VÀO: RSI = 22.3 < 30. Thị trường đang trong vùng quá bán, có khả năng cao sẽ có phục hồi tăng.
KHUYẾN NGHỊ: Chuẩn bị cho sự đảo chiều với độ tin cậy CAO. Cân nhắc chốt lời hoặc cắt lỗ.
═══════════════════════════════════════════════════════

8. TEST TÍN HIỆU GIAO CẮT EMA:
   a) EMA20 cắt lên EMA50 (Golden Cross):
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: GOLD (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 90.00 → 95.67 VND ( +6.30% - TĂNG)
EMA 20: 94.23 VND
EMA 50: 92.45 VND
EMA 20 > EMA 50: Xu hướng tăng
RSI: 56.8 (TÍCH CỰC - Động lực tốt)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG TĂNG (EMA 20: 94.23 > EMA 50: 92.45, chênh lệch: +1.93%). 
ĐỘNG LỰC TÍCH CỰC (RSI: 56.8) - Thị trường khỏe mạnh
TÍN HIỆU: MUA (LONG) (Độ tin cậy: 76.2%)
LÝ DO: TÍN HIỆU MUA: EMA 20 (94.23) cắt lên EMA 50 (92.45), RSI: 56.8 > 55. Xác nhận bởi MACD cắt lên.
KHUYẾN NGHỊ: Mua vào với độ tin cậy CAO. Đặt stop-loss dưới mức hỗ trợ gần nhất.
═══════════════════════════════════════════════════════

   b) EMA20 cắt xuống EMA50 (Death Cross):
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: DEATH (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 140.00 → 125.34 VND ( -10.47% - GIẢM)
EMA 20: 123.67 VND
EMA 50: 128.45 VND
EMA 20 < EMA 50: Xu hướng giảm
RSI: 42.1 (TRUNG TÍNH - Chờ tín hiệu)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG GIẢM (EMA 20: 123.67 < EMA 50: 128.45, chênh lệch: -3.72%). 
TRUNG TÍNH (RSI: 42.1) - Chờ tín hiệu rõ ràng hơn
TÍN HIỆU: BÁN (SHORT) (Độ tin cậy: 69.5%)
LÝ DO: TÍN HIỆU BÁN: EMA 20 (123.67) cắt xuống EMA 50 (128.45), RSI: 42.1 < 45. Xác nhận bởi MACD cắt xuống.
KHUYẾN NGHỊ: Bán ra với độ tin cậy CAO. Đặt stop-loss trên mức kháng cự gần nhất.
═══════════════════════════════════════════════════════

10. TEST XU HƯỚNG NHẸ (TĂNG/GIẢM NHẸ):
═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: MILDUP (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 100.00 → 104.12 VND ( +4.12% - TĂNG)
EMA 20: 103.45 VND
EMA 50: 101.23 VND
EMA 20 > EMA 50: Xu hướng tăng
RSI: 52.3 (TÍCH CỰC - Động lực tốt)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG TĂNG (EMA 20: 103.45 > EMA 50: 101.23, chênh lệch: +2.19%). 
ĐỘNG LỰC TÍCH CỰC (RSI: 52.3) - Thị trường khỏe mạnh
TÍN HIỆU: HOLD (GIỮ) - Không có tín hiệu giao dịch rõ ràng
KHUYẾN NGHỊ: Tiếp tục theo dõi và chờ tín hiệu rõ ràng hơn
═══════════════════════════════════════════════════════

═══════════════════════════════════════════════════════
MÃ CỔ PHIẾU: MILDDOWN (60 điểm dữ liệu)
BIẾN ĐỘNG GIÁ: 120.00 → 115.23 VND ( -3.98% - GIẢM)
EMA 20: 114.67 VND
EMA 50: 116.45 VND
EMA 20 < EMA 50: Xu hướng giảm
RSI: 47.8 (TRUNG TÍNH - Chờ tín hiệu)

PHÂN TÍCH DỰ ĐOÁN:
TÂM LÝ THỊ TRƯỜNG: XU HƯỚNG GIẢM (EMA 20: 114.67 < EMA 50: 116.45, chênh lệch: -1.53%). 
TRUNG TÍNH (RSI: 47.8) - Chờ tín hiệu rõ ràng hơn
TÍN HIỆU: HOLD (GIỮ) - Không có tín hiệu giao dịch rõ ràng
KHUYẾN NGHỊ: Tiếp tục theo dõi và chờ tín hiệu rõ ràng hơn
═══════════════════════════════════════════════════════
```

## Quy tắc tín hiệu

| Tín hiệu | Điều kiện | Ý nghĩa |
|----------|-----------|---------|
| **LONG** | EMA20 cắt lên EMA50 + RSI > 55 | Mua |
| **SHORT** | EMA20 cắt xuống EMA50 + RSI < 45 | Bán |
| **REVERSAL** | RSI > 70 (quá mua) hoặc RSI < 30 (quá bán) | Đảo chiều |
| **HOLD** | Không đủ điều kiện | Chờ tín hiệu |

## 🔧 Chỉ báo kỹ thuật
- **EMA 20/50**: Đường trung bình động
- **RSI 14**: Chỉ số sức mạnh tương đối  
- **MACD 12,26,9**: Hội tụ phân kỳ
