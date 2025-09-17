package main;


import main.stockprediction.model.StockData;
import main.stockprediction.model.PredictionSignal;
import main.stockprediction.engine.PredictionEngine;
import main.stockprediction.data.StockDataGenerator;
import main.stockprediction.indicators.TechnicalIndicators;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class for Stock Trend Prediction System
 */
public class StockPredictionApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PredictionEngine predictionEngine = new PredictionEngine();

    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG DỰ ĐOÁN XU HƯỚNG CHỨNG KHOÁN ===");
        System.out.println("Stock Trend Prediction System");
        System.out.println("Sử dụng các chỉ báo kỹ thuật: EMA, RSI, MACD");
        System.out.println("============================================\n");

        while (true) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    analyzeRandomData();
                    break;
                case 2:
                    analyzePatternData();
                    break;
                case 3:
                    showTechnicalIndicators();
                    break;
                case 4:
                    showPredictionRules();
                    break;
                case 0:
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }

            System.out.println("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== MENU CHÍNH ===");
        System.out.println("1. Phân tích dữ liệu ngẫu nhiên");
        System.out.println("2. Phân tích dữ liệu theo mẫu");
        System.out.println("3. Hiển thị chỉ báo kỹ thuật");
        System.out.println("4. Hiển thị quy tắc dự đoán");
        System.out.println("0. Thoát");
        System.out.print("Chọn: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void analyzeRandomData() {
        System.out.println("\n=== PHÂN TÍCH DỮ LIỆU NGẪU NHIÊN ===");

        // Generate sample data
        List<StockData> stockData = StockDataGenerator.generateSampleData(100, 100.0);

        System.out.println("Đã tạo " + stockData.size() + " điểm dữ liệu");
        System.out.println("Giá bắt đầu: " + String.format("%.2f VND", stockData.get(0).getClose()));
        System.out.println("Giá hiện tại: " + String.format("%.2f VND", stockData.get(stockData.size() - 1).getClose()));

        // Analyze trend
        List<PredictionSignal> signals = predictionEngine.analyzeTrend(stockData);

        System.out.println("\n--- TÌNH HÌNH THỊ TRƯỜNG ---");
        System.out.println(predictionEngine.getMarketSentiment(stockData));

        System.out.println("\n--- TÍN HIỆU GIAO DỊCH ---");
        if (signals.isEmpty()) {
            System.out.println("Không có tín hiệu giao dịch nào được tạo ra.");
        } else {
            System.out.println("Tìm thấy " + signals.size() + " tín hiệu:");
            for (PredictionSignal signal : signals) {
                System.out.println("• " + signal);
            }
        }
    }

    private static void analyzePatternData() {
        System.out.println("\n=== PHÂN TÍCH DỮ LIỆU THEO MẪU ===");
        System.out.println("Chọn loại mẫu:");
        System.out.println("1. Tăng giá (Bullish)");
        System.out.println("2. Giảm giá (Bearish)");
        System.out.println("3. Đi ngang (Sideways)");
        System.out.println("4. Biến động cao (Volatile)");
        System.out.print("Chọn: ");

        int patternChoice = getChoice();
        String pattern;

        switch (patternChoice) {
            case 1: pattern = "bullish"; break;
            case 2: pattern = "bearish"; break;
            case 3: pattern = "sideways"; break;
            case 4: pattern = "volatile"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        List<StockData> stockData = StockDataGenerator.generatePatternData(80, 100.0, pattern);

        System.out.println("\nĐã tạo dữ liệu mẫu: " + pattern.toUpperCase());
        System.out.println("Số điểm dữ liệu: " + stockData.size());
        System.out.println("Giá bắt đầu: " + String.format("%.2f VND", stockData.get(0).getClose()));
        System.out.println("Giá kết thúc: " + String.format("%.2f VND", stockData.get(stockData.size() - 1).getClose()));

        // Analyze trend
        List<PredictionSignal> signals = predictionEngine.analyzeTrend(stockData);

        System.out.println("\n--- TÌNH HÌNH THỊ TRƯỜNG ---");
        System.out.println(predictionEngine.getMarketSentiment(stockData));

        System.out.println("\n--- TÍN HIỆU GIAO DỊCH ---");
        if (signals.isEmpty()) {
            System.out.println("Không có tín hiệu giao dịch nào được tạo ra.");
        } else {
            System.out.println("Tìm thấy " + signals.size() + " tín hiệu:");
            for (PredictionSignal signal : signals) {
                System.out.println("• " + signal);
            }
        }
    }

    private static void showTechnicalIndicators() {
        System.out.println("\n=== CHỈ BÁO KỸ THUẬT ===");

        // Generate sample data for demonstration
        List<StockData> stockData = StockDataGenerator.generateSampleData(30, 100.0);

        // Calculate indicators
        List<Double> ema20 = TechnicalIndicators.calculateEMA(stockData, 20);
        List<Double> ema50 = TechnicalIndicators.calculateEMA(stockData, 50);
        List<Double> rsi = TechnicalIndicators.calculateRSI(stockData, 14);
        TechnicalIndicators.MACDResult macd = TechnicalIndicators.calculateMACD(stockData, 12, 26, 9);

        System.out.println("Hiển thị 5 điểm dữ liệu gần nhất:");
        System.out.println("Date\t\tPrice\t\tEMA20\t\tEMA50\t\tRSI\t\tMACD");
        System.out.println("--------------------------------------------------------------------");

        for (int i = Math.max(0, stockData.size() - 5); i < stockData.size(); i++) {
            StockData data = stockData.get(i);
            System.out.printf("%s\t%.2f\t\t%.2f\t\t%.2f\t\t%.2f\t\t%.4f%n",
                    data.getTimestamp().toLocalDate(),
                    data.getClose(),
                    ema20.get(i) != null ? ema20.get(i) : 0.0,
                    ema50.get(i) != null ? ema50.get(i) : 0.0,
                    rsi.get(i) != null ? rsi.get(i) : 0.0,
                    macd.getMacdLine().get(i) != null ? macd.getMacdLine().get(i) : 0.0
            );
        }
    }

    private static void showPredictionRules() {
        System.out.println("\n=== QUY TẮC DỰ ĐOÁN ===");
        System.out.println("Hệ thống sử dụng các quy tắc sau để tạo tín hiệu giao dịch:");
        System.out.println();

        System.out.println("1. TÍN HIỆU MUA (LONG):");
        System.out.println("   • EMA 20 cắt lên trên EMA 50");
        System.out.println("   • RSI > 55");
        System.out.println("   → Xu hướng tăng giá");
        System.out.println();

        System.out.println("2. TÍN HIỆU BÁN (SHORT):");
        System.out.println("   • EMA 20 cắt xuống dưới EMA 50");
        System.out.println("   • RSI < 45");
        System.out.println("   → Xu hướng giảm giá");
        System.out.println();

        System.out.println("3. TÍN HIỆU ĐẢO CHIỀU (REVERSAL):");
        System.out.println("   • RSI > 70: Quá mua (có thể giảm giá)");
        System.out.println("   • RSI < 30: Quá bán (có thể tăng giá)");
        System.out.println();

        System.out.println("4. CHỈ BÁO KỸ THUẬT:");
        System.out.println("   • EMA 20: Đường trung bình động 20 ngày");
        System.out.println("   • EMA 50: Đường trung bình động 50 ngày");
        System.out.println("   • RSI: Chỉ số sức mạnh tương đối (14 ngày)");
        System.out.println("   • MACD: Hội tụ phân kỳ đường trung bình");
    }
}
