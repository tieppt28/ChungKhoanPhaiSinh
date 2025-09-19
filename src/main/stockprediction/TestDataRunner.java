package src.main.stockprediction;

import main.stockprediction.data.StockDataGenerator;
import src.main.stockprediction.engine.PredictionEngine;
import main.stockprediction.indicators.TechnicalIndicators;
import main.stockprediction.ui.CandlestickChart;
import main.stockprediction.model.PredictionSignal;
import main.stockprediction.model.StockData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test runner for various fake data scenarios
 */
public class TestDataRunner {

    public static void main(String[] args) {
        System.out.println("=== CHẠY TEST DỮ LIỆU DỰ ĐOÁN TOÀN DIỆN ===\n");

        // Test 1: Xu hướng tăng mạnh (Strong Bullish)
        testStrongBullishTrend();

        // Test 2: Xu hướng giảm mạnh (Strong Bearish)
        testStrongBearishTrend();

        // Test 3: Thị trường đi ngang (Sideways)
        testSidewaysMarket();

        // Test 4: Thị trường biến động cao (High Volatility)
        testHighVolatilityMarket();

        // Test 5: Điều kiện RSI quá mua/quá bán
        testOverboughtOversoldConditions();

        // Test 6: Tín hiệu đảo chiều tại support/resistance
        testReversalSignals();

        // Test 7: Các kịch bản thị trường cực đoan
        testExtremeMarketScenarios();

        // Test 8: Tín hiệu giao cắt EMA
        testEMACrossoverSignals();

        // Test 9: Tổng hợp tất cả các loại tín hiệu
        testAllSignalTypes();

        // Test 10: Xu hướng nhẹ (tăng/giảm nhẹ) để đa dạng phân tích
        testMildTrendScenarios();
    }

    private static void testStrongBullishTrend() {
        System.out.println("1. TEST XU HƯỚNG TĂNG MẠNH:");
        List<StockData> data = StockDataGenerator.generatePatternData(80, 100.0, "bullish");
        analyzeAndPredict(data, "BULL");
        // Hiển thị chart nến cho bộ dữ liệu đầu tiên
        CandlestickChart.showCandles("Candles - BULL", data);
        System.out.println();
    }

    private static void testStrongBearishTrend() {
        System.out.println("2. TEST XU HƯỚNG GIẢM MẠNH:");
        List<StockData> data = StockDataGenerator.generatePatternData(80, 150.0, "bearish");
        analyzeAndPredict(data, "BEAR");
        System.out.println();
    }

    private static void testSidewaysMarket() {
        System.out.println("3. TEST THỊ TRƯỜNG ĐI NGANG:");
        List<StockData> data = StockDataGenerator.generatePatternData(80, 120.0, "sideways");
        analyzeAndPredict(data, "SIDE");
        System.out.println();
    }

    private static void testHighVolatilityMarket() {
        System.out.println("4. TEST THỊ TRƯỜNG BIẾN ĐỘNG CAO:");
        List<StockData> data = StockDataGenerator.generatePatternData(80, 80.0, "volatile");
        analyzeAndPredict(data, "VOL");
        System.out.println();
    }

    private static void testOverboughtOversoldConditions() {
        System.out.println("5. TEST ĐIỀU KIỆN QUÁ MUA/QUÁ BÁN:");

        // Tạo dữ liệu có RSI cao (quá mua)
        System.out.println("   a) Điều kiện quá mua (RSI > 70):");
        List<StockData> overboughtData = createOverboughtData(60, 100.0);
        analyzeAndPredict(overboughtData, "OVER");

        // Tạo dữ liệu có RSI thấp (quá bán)
        System.out.println("   b) Điều kiện quá bán (RSI < 30):");
        List<StockData> oversoldData = createOversoldData(60, 100.0);
        analyzeAndPredict(oversoldData, "UNDER");

        System.out.println();
    }

    private static void testReversalSignals() {
        System.out.println("6. TEST TÍN HIỆU ĐẢO CHIỀU:");
        List<StockData> data = StockDataGenerator.generateDataWithSupportResistance(60, 130.0);
        analyzeAndPredict(data, "REV");
        System.out.println();
    }

    private static void testExtremeMarketScenarios() {
        System.out.println("7. TEST CÁC KỊCH BẢN THỊ TRƯỜNG CỰC ĐOAN:");

        System.out.println("   a) Sụp đổ thị trường:");
        List<StockData> crashData = StockDataGenerator.generateExtremeMarketData(60, 200.0, "crash");
        analyzeAndPredict(crashData, "CRASH");

        System.out.println("   b) Bong bóng thị trường:");
        List<StockData> bubbleData = StockDataGenerator.generateExtremeMarketData(60, 50.0, "bubble");
        analyzeAndPredict(bubbleData, "BUBBLE");

        System.out.println("   c) Flash crash:");
        List<StockData> flashData = StockDataGenerator.generateExtremeMarketData(60, 120.0, "flash_crash");
        analyzeAndPredict(flashData, "FLASH");

        System.out.println();
    }

    private static void testEMACrossoverSignals() {
        System.out.println("8. TEST TÍN HIỆU GIAO CẮT EMA:");

        // Tạo dữ liệu có EMA20 cắt lên EMA50
        System.out.println("   a) EMA20 cắt lên EMA50 (Golden Cross):");
        List<StockData> goldenCrossData = createGoldenCrossData(60, 90.0);
        analyzeAndPredict(goldenCrossData, "GOLD");

        // Tạo dữ liệu có EMA20 cắt xuống EMA50
        System.out.println("   b) EMA20 cắt xuống EMA50 (Death Cross):");
        List<StockData> deathCrossData = createDeathCrossData(60, 140.0);
        analyzeAndPredict(deathCrossData, "DEATH");

        System.out.println();
    }

    private static void analyzeAndPredict(List<StockData> data, String symbol) {
        if (data.isEmpty()) return;

        StockData first = data.get(0);
        StockData last = data.get(data.size() - 1);
        double totalChange = ((last.getClose() - first.getClose()) / first.getClose()) * 100;

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.printf(" MÃ CỔ PHIẾU: %s (%d điểm dữ liệu)%n", symbol, data.size());
        System.out.printf(" BIẾN ĐỘNG GIÁ: %.2f → %.2f VND ", first.getClose(), last.getClose());

        if (totalChange > 0) {
            System.out.printf("( +%.2f%% - TĂNG)%n", totalChange);
        } else if (totalChange < 0) {
            System.out.printf("( %.2f%% - GIẢM)%n", totalChange);
        } else {
            System.out.printf("( %.2f%% - KHÔNG ĐỔI)%n", totalChange);
        }

        // Tính toán và hiển thị chỉ báo kỹ thuật
        if (data.size() >= 20) {
            List<Double> ema20 = TechnicalIndicators.calculateEMA(data, 20);
            List<Double> ema50 = TechnicalIndicators.calculateEMA(data, 50);
            List<Double> rsi = TechnicalIndicators.calculateRSI(data, 14);

            if (!ema20.isEmpty() && !rsi.isEmpty()) {
                Double lastEMA20 = ema20.get(ema20.size() - 1);
                Double lastRSI = rsi.get(rsi.size() - 1);

                if (lastEMA20 != null && lastRSI != null) {
                    System.out.printf(" EMA 20: %.2f VND%n", lastEMA20);

                    if (data.size() >= 50 && !ema50.isEmpty()) {
                        Double lastEMA50 = ema50.get(ema50.size() - 1);
                        if (lastEMA50 != null) {
                            System.out.printf(" EMA 50: %.2f VND%n", lastEMA50);
                            if (lastEMA20 > lastEMA50) {
                                System.out.println(" EMA 20 > EMA 50: Xu hướng tăng");
                            } else {
                                System.out.println(" EMA 20 < EMA 50: Xu hướng giảm");
                            }
                        }
                    }

                    System.out.printf(" RSI: %.1f ", lastRSI);
                    if (lastRSI > 70) {
                        System.out.println("( QUÁ MUA - Cảnh báo điều chỉnh)");
                    } else if (lastRSI < 30) {
                        System.out.println("( QUÁ BÁN - Cơ hội mua vào)");
                    } else if (lastRSI > 50) {
                        System.out.println("( TÍCH CỰC - Động lực tốt)");
                    } else {
                        System.out.println("( TRUNG TÍNH - Chờ tín hiệu)");
                    }
                }
            }
        }

        // Phân tích và dự đoán
        PredictionEngine engine = new PredictionEngine();
        System.out.println("\n PHÂN TÍCH DỰ ĐOÁN:");

        // Hiển thị tâm lý thị trường
        String sentiment = engine.getMarketSentiment(data);
        System.out.println(" TÂM LÝ THỊ TRƯỜNG: " + sentiment);

        List<PredictionSignal> signals = engine.analyzeTrend(data);
        if (!signals.isEmpty()) {
            PredictionSignal signal = signals.get(signals.size() - 1);
            System.out.printf(" TÍN HIỆU: %s (Độ tin cậy: %.1f%%)%n",
                    translateSignalType(signal.getSignalType()), signal.getConfidence() * 100);
            System.out.println(" LÝ DO: " + signal.getReason());
            System.out.println(" KHUYẾN NGHỊ: " + getRecommendation(signal.getSignalType(), signal.getConfidence()));
        } else {
            System.out.println(" TÍN HIỆU: HOLD (GIỮ) - Không có tín hiệu giao dịch rõ ràng");
            System.out.println(" KHUYẾN NGHỊ: Tiếp tục theo dõi và chờ tín hiệu rõ ràng hơn");
        }

        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static String translateSignalType(PredictionSignal.SignalType signalType) {
        switch (signalType) {
            case LONG:
                return "MUA (LONG) ";
            case SHORT:
                return "BÁN (SHORT) ";
            case REVERSAL:
                return "ĐẢO CHIỀU (REVERSAL) ";
            case HOLD:
                return "GIỮ (HOLD) ";
            default:
                return "KHÔNG XÁC ĐỊNH ";
        }
    }

    private static String getRecommendation(PredictionSignal.SignalType signalType, double confidence) {
        String confidenceLevel;
        if (confidence > 0.8) {
            confidenceLevel = "RẤT CAO";
        } else if (confidence > 0.6) {
            confidenceLevel = "CAO";
        } else if (confidence > 0.4) {
            confidenceLevel = "TRUNG BÌNH";
        } else {
            confidenceLevel = "THẤP";
        }

        switch (signalType) {
            case LONG:
                return String.format("Mua vào với độ tin cậy %s. Đặt stop-loss dưới mức hỗ trợ gần nhất.", confidenceLevel);
            case SHORT:
                return String.format("Bán ra với độ tin cậy %s. Đặt stop-loss trên mức kháng cự gần nhất.", confidenceLevel);
            case REVERSAL:
                return String.format("Chuẩn bị cho sự đảo chiều với độ tin cậy %s. Cân nhắc chốt lời hoặc cắt lỗ.", confidenceLevel);
            case HOLD:
                return "Giữ nguyên vị thế hiện tại và tiếp tục theo dõi diễn biến thị trường.";
            default:
                return "Cần thêm dữ liệu để đưa ra khuyến nghị chính xác.";
        }
    }

    private static void testAllSignalTypes() {
        System.out.println("9. TEST TỔNG HỢP TẤT CẢ CÁC LOẠI TÍN HIỆU:");

        String[] patterns = {"bullish", "bearish", "sideways", "volatile"};
        String[] symbols = {"VNM", "VIC", "VHM", "VCB"};
        String[] descriptions = {
                "Xu hướng tăng mạnh - Vinamilk",
                "Xu hướng giảm mạnh - Vingroup",
                "Thị trường đi ngang - Vinhomes",
                "Biến động cao - Vietcombank"
        };

        for (int i = 0; i < patterns.length; i++) {
            System.out.printf("   %s (%s):%n", descriptions[i], patterns[i]);
            List<StockData> data = StockDataGenerator.generatePatternData(60, 100 + i * 20, patterns[i]);
            analyzeAndPredict(data, symbols[i]);
        }

        System.out.println(" KIẾN THỨC CƠ BẢN VỀ TÍN HIỆU GIAO DỊCH:");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println(" LONG (MUA): EMA20 > EMA50 và RSI > 55");
        System.out.println("   → Xu hướng tăng được xác nhận với động lực mạnh");
        System.out.println(" SHORT (BÁN): EMA20 < EMA50 và RSI < 45");
        System.out.println("   → Xu hướng giảm được xác nhận với áp lực bán lớn");
        System.out.println("️ REVERSAL (ĐẢO CHIỀU): RSI > 70 (quá mua) hoặc RSI < 30 (quá bán)");
        System.out.println("   → Thị trường có thể đảo chiều, cần cảnh giác");
        System.out.println(" HOLD (GIỮ): Không đủ điều kiện cho các tín hiệu trên");
        System.out.println("   → Tiếp tục theo dõi và chờ tín hiệu rõ ràng");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    private static void testMildTrendScenarios() {
        System.out.println("10. TEST XU HƯỚNG NHẸ (TĂNG/GIẢM NHẸ):");

        // Tăng nhẹ tổng thể ~4% trong giai đoạn
        List<StockData> mildUp = StockDataGenerator.generateTrendingData(60, 100.0, 0.04);
        analyzeAndPredict(mildUp, "MILDUP");

        // Giảm nhẹ tổng thể ~4% trong giai đoạn
        List<StockData> mildDown = StockDataGenerator.generateTrendingData(60, 120.0, -0.04);
        analyzeAndPredict(mildDown, "MILDDOWN");

        System.out.println();
    }

    private static List<StockData> createOverboughtData(int days, double startPrice) {
        List<StockData> data = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(days);
        double currentPrice = startPrice;

        // Tạo xu hướng tăng mạnh để RSI > 70
        for (int i = 0; i < days; i++) {
            double trendStrength = 0.03; // 3% tăng mỗi ngày
            double change = trendStrength * currentPrice + (Math.random() - 0.3) * 0.01 * currentPrice;

            double open = currentPrice;
            double close = currentPrice + change;
            double high = Math.max(open, close) + Math.random() * 0.005 * currentPrice;
            double low = Math.min(open, close) - Math.random() * 0.005 * currentPrice;
            long volume = 100000 + (int)(Math.random() * 500000);

            data.add(new StockData(currentTime, open, high, low, close, volume));
            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return data;
    }

    private static List<StockData> createOversoldData(int days, double startPrice) {
        List<StockData> data = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(days);
        double currentPrice = startPrice;

        // Tạo xu hướng giảm mạnh để RSI < 30
        for (int i = 0; i < days; i++) {
            double trendStrength = -0.025; // 2.5% giảm mỗi ngày
            double change = trendStrength * currentPrice + (Math.random() - 0.7) * 0.01 * currentPrice;

            double open = currentPrice;
            double close = Math.max(1.0, currentPrice + change); // Không cho giá âm
            double high = Math.max(open, close) + Math.random() * 0.005 * currentPrice;
            double low = Math.min(open, close) - Math.random() * 0.005 * currentPrice;
            long volume = 150000 + (int)(Math.random() * 600000);

            data.add(new StockData(currentTime, open, high, low, close, volume));
            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return data;
    }

    private static List<StockData> createGoldenCrossData(int days, double startPrice) {
        List<StockData> data = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(days);
        double currentPrice = startPrice;

        for (int i = 0; i < days; i++) {
            double change;
            if (i < days / 2) {
                // Nửa đầu: giảm nhẹ hoặc đi ngang
                change = (Math.random() - 0.6) * 0.01 * currentPrice;
            } else {
                // Nửa sau: tăng mạnh để tạo golden cross
                change = 0.02 * currentPrice + (Math.random() - 0.2) * 0.01 * currentPrice;
            }

            double open = currentPrice;
            double close = currentPrice + change;
            double high = Math.max(open, close) + Math.random() * 0.008 * currentPrice;
            double low = Math.min(open, close) - Math.random() * 0.008 * currentPrice;
            long volume = 120000 + (int)(Math.random() * 700000);

            data.add(new StockData(currentTime, open, high, low, close, volume));
            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return data;
    }

    private static List<StockData> createDeathCrossData(int days, double startPrice) {
        List<StockData> data = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(days);
        double currentPrice = startPrice;

        for (int i = 0; i < days; i++) {
            double change;
            if (i < days / 2) {
                // Nửa đầu: tăng nhẹ hoặc đi ngang
                change = (Math.random() - 0.4) * 0.01 * currentPrice;
            } else {
                // Nửa sau: giảm mạnh để tạo death cross
                change = -0.018 * currentPrice + (Math.random() - 0.8) * 0.01 * currentPrice;
            }

            double open = currentPrice;
            double close = Math.max(1.0, currentPrice + change);
            double high = Math.max(open, close) + Math.random() * 0.008 * currentPrice;
            double low = Math.min(open, close) - Math.random() * 0.008 * currentPrice;
            long volume = 180000 + (int)(Math.random() * 800000);

            data.add(new StockData(currentTime, open, high, low, close, volume));
            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return data;
    }
}
