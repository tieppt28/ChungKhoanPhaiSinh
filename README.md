## Tài liệu kiến trúc và thành phần

### Tổng quan hệ thống
- **Nguồn dữ liệu**: `main.stockprediction.data.StockDataGenerator` tạo dữ liệu OHLCV giả lập theo nhiều kịch bản (daily, intraday, trending, sideways, volatile, gap, hỗ trợ/kháng cự, điều kiện cực đoan).
- **Chỉ báo kỹ thuật**: `main.stockprediction.indicators.TechnicalIndicators` tính `SMA`, `EMA`, `RSI`, `MACD` và histogram.
- **Máy dự đoán**: `main.stockprediction.engine.PredictionEngine` sinh `PredictionSignal` (LONG/SHORT/REVERSAL) từ các chỉ báo và mô tả tâm lý thị trường.

### Chi tiết từng class và method chính

#### main.stockprediction.data.StockDataGenerator
- **Vai trò**: Sinh dữ liệu chứng khoán giả lập phục vụ thử nghiệm.
- **Method công khai**:
  - `generateSampleData(int days, double startPrice) -> List<StockData>`: dữ liệu daily biến động ~2%/ngày.
  - `generateTrendingData(int days, double startPrice, double trendStrength) -> List<StockData>`: thêm xu hướng tăng/giảm + nhiễu.
  - `generatePatternData(int days, double startPrice, String pattern) -> List<StockData>`: chọn `bullish|bearish|sideways|volatile`.
  - `generateMultipleStocksData(int numberOfStocks, int days) -> List<List<StockData>>`: tạo nhiều mã với pattern ngẫu nhiên.
  - `generateIntradayData(int hours, double startPrice) -> List<StockData>`: dữ liệu theo giờ, biến động/khối lượng thấp hơn.
  - `generateDataWithGaps(int days, double startPrice) -> List<StockData>`: thêm gap up/down (xác suất ~10%).
  - `generateDataWithSupportResistance(int days, double startPrice) -> List<StockData>`: bật/tụt quanh hỗ trợ 0.9x và kháng cự 1.1x.
  - `generateExtremeMarketData(int days, double startPrice, String condition) -> List<StockData>`: `crash|bubble|flash_crash`.
  - `getStockSymbol(int index)`, `getCompanyName(int index)`.
- **Lưu ý**: Danh sách chỉ số trả về theo `LocalDateTime` tăng dần; mỗi phần tử có đủ `open/high/low/close/volume`.

#### main.stockprediction.indicators.TechnicalIndicators
- **Vai trò**: Tính toán chỉ báo kỹ thuật chuẩn.
- **Method công khai**:
  - `calculateSMA(List<StockData> data, int period) -> List<Double>`: trả `null` ở vị trí chưa đủ dữ liệu.
  - `calculateEMA(List<StockData> data, int period) -> List<Double>`: seed bằng SMA tại chỉ số `period-1`, trước đó là `null`.
  - `calculateRSI(List<StockData> data, int period) -> List<Double>`: Wilder’s smoothing; giai đoạn khởi tạo trả `null`, giá trị đầu tại `i==period-1`.
  - `calculateMACD(List<StockData> data, int fast, int slow, int signal) -> MACDResult` với `getMacdLine()`, `getSignalLine()`, `getHistogram()`.
- **Lưu ý**: Kết quả thẳng hàng với dữ liệu đầu vào; luôn kiểm tra `null` trước khi dùng.

#### main.stockprediction.engine.PredictionEngine
- **Vai trò**: Phân tích xu hướng và sinh tín hiệu.
- **Hằng số**: `EMA_20_PERIOD=20`, `EMA_50_PERIOD=50`, `RSI_PERIOD=14`, `MACD_FAST=12`, `MACD_SLOW=26`, `MACD_SIGNAL=9`.
- **Method công khai**:
  - `analyzeTrend(List<StockData> data) -> List<PredictionSignal>`: tính EMA/RSI/MACD, duyệt từ chỉ số 50, ưu tiên giao cắt EMA có xác nhận MACD và ngưỡng RSI; nếu không có thì kiểm tra quá mua/quá bán để sinh REVERSAL.
  - `getMarketSentiment(List<StockData> data) -> String`: mô tả xu hướng (EMA20/EMA50) + trạng thái RSI (quá mua/bán/tích cực/trung tính).
- **Method nội bộ**:
  - `checkEMACrossover(...)`: LONG khi `EMA20` cắt lên `EMA50` và `RSI>55`; SHORT khi cắt xuống và `RSI<45`. Độ tin cậy là tổng hợp có trọng số: RSI (55%), EMA spread (25%), xác nhận MACD (15%), độ dốc histogram (5%).
  - `checkOverboughtOversold(...)`: RSI>70 hoặc <30 sinh cảnh báo đảo chiều.

### Luồng hoạt động
1. Tạo dữ liệu: dùng `StockDataGenerator.*` để lấy `List<StockData>` theo kịch bản mong muốn.
2. Phân tích: gọi `PredictionEngine.analyzeTrend(data)` để lấy danh sách `PredictionSignal`.
3. Tóm tắt: dùng `PredictionEngine.getMarketSentiment(data)` để mô tả bối cảnh thị trường.

### Ví dụ sử dụng nhanh
```java
import java.util.List;
import main.stockprediction.data.StockDataGenerator;
import main.stockprediction.engine.PredictionEngine;
import main.stockprediction.model.StockData;
import main.stockprediction.model.PredictionSignal;

List<StockData> data = StockDataGenerator.generatePatternData(200, 50.0, "bullish");
PredictionEngine engine = new PredictionEngine();
List<PredictionSignal> signals = engine.analyzeTrend(data);
String sentiment = engine.getMarketSentiment(data);
```


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

##  Chỉ báo kỹ thuật
- **EMA 20/50**: Đường trung bình động
- **RSI 14**: Chỉ số sức mạnh tương đối  
- **MACD 12,26,9**: Hội tụ phân kỳ
