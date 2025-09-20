package stockprediction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockprediction.entity.StockDataEntity;
import stockprediction.entity.PredictionSignalEntity;
import stockprediction.repository.StockDataRepository;
import stockprediction.repository.PredictionSignalRepository;
import stockprediction.indicators.TechnicalIndicators;
import stockprediction.engine.PredictionEngine;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for comprehensive market analysis
 */
@Service
public class MarketAnalysisService {
    
    @Autowired
    private StockDataRepository stockDataRepository;
    
    @Autowired
    private PredictionSignalRepository predictionSignalRepository;
    
    private final TechnicalIndicators technicalIndicators = new TechnicalIndicators();
    
    private final PredictionEngine predictionEngine = new PredictionEngine();
    
    /**
     * Get comprehensive market analysis for TradingView
     */
    public Map<String, Object> getMarketAnalysis(String symbol) {
        try {
            // Get recent stock data
            List<StockDataEntity> recentData = stockDataRepository
                .findLatestBySymbol(symbol)
                .stream()
                .limit(100)
                .collect(Collectors.toList());
            
            if (recentData.isEmpty()) {
                return generateMockAnalysis(symbol);
            }
            
            // Calculate technical indicators
            Map<String, Object> analysis = calculateTechnicalAnalysis(recentData);
            
            // Generate prediction signal
            Map<String, Object> signal = generatePredictionSignal(recentData, analysis);
            
            // Market sentiment analysis
            Map<String, Object> sentiment = analyzeMarketSentiment(recentData);
            
            // Combine all analysis
            Map<String, Object> result = new HashMap<>();
            result.put("symbol", symbol);
            result.put("timestamp", LocalDateTime.now());
            result.put("technicalAnalysis", analysis);
            result.put("predictionSignal", signal);
            result.put("marketSentiment", sentiment);
            result.put("dataQuality", "real");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Error in market analysis for " + symbol + ": " + e.getMessage());
            return generateMockAnalysis(symbol);
        }
    }
    
    /**
     * Calculate technical analysis indicators
     */
    private Map<String, Object> calculateTechnicalAnalysis(List<StockDataEntity> data) {
        Map<String, Object> analysis = new HashMap<>();
        
        try {
            // Convert to arrays for calculations
            double[] closes = data.stream()
                .mapToDouble(StockDataEntity::getClose)
                .toArray();
            double[] highs = data.stream()
                .mapToDouble(StockDataEntity::getHigh)
                .toArray();
            double[] lows = data.stream()
                .mapToDouble(StockDataEntity::getLow)
                .toArray();
            double[] volumes = data.stream()
                .mapToDouble(StockDataEntity::getVolume)
                .toArray();
            
            // Convert to StockData list for technical indicators
            List<stockprediction.model.StockData> stockDataList = data.stream()
                .map(entity -> new stockprediction.model.StockData(
                    entity.getTimestamp(),
                    entity.getOpen(),
                    entity.getHigh(),
                    entity.getLow(),
                    entity.getClose(),
                    entity.getVolume()
                ))
                .collect(Collectors.toList());
            
            // Calculate RSI
            List<Double> rsiList = technicalIndicators.calculateRSI(stockDataList, 14);
            double rsi = rsiList.isEmpty() ? 50.0 : rsiList.get(rsiList.size() - 1);
            analysis.put("rsi", rsi);
            analysis.put("rsiSignal", getRSISignal(rsi));
            
            // Calculate MACD
            TechnicalIndicators.MACDResult macd = technicalIndicators.calculateMACD(stockDataList, 12, 26, 9);
            double macdValue = macd.getMacdLine().isEmpty() ? 0.0 : macd.getMacdLine().get(macd.getMacdLine().size() - 1);
            double signalValue = macd.getSignalLine().isEmpty() ? 0.0 : macd.getSignalLine().get(macd.getSignalLine().size() - 1);
            double histogramValue = macd.getHistogram().isEmpty() ? 0.0 : macd.getHistogram().get(macd.getHistogram().size() - 1);
            
            analysis.put("macd", macdValue);
            analysis.put("macdSignal", signalValue);
            analysis.put("macdHistogram", histogramValue);
            analysis.put("macdTrend", getMACDTrend(macdValue, signalValue, histogramValue));
            
            // Calculate EMAs
            List<Double> ema20List = technicalIndicators.calculateEMA(stockDataList, 20);
            List<Double> ema50List = technicalIndicators.calculateEMA(stockDataList, 50);
            double ema20 = ema20List.isEmpty() ? 100.0 : ema20List.get(ema20List.size() - 1);
            double ema50 = ema50List.isEmpty() ? 100.0 : ema50List.get(ema50List.size() - 1);
            analysis.put("ema20", ema20);
            analysis.put("ema50", ema50);
            analysis.put("emaTrend", getEMATrend(ema20, ema50));
            
            // Calculate Bollinger Bands (simplified)
            double currentPrice = closes[closes.length - 1];
            double avgPrice = Arrays.stream(closes).average().orElse(currentPrice);
            double stdDev = Math.sqrt(Arrays.stream(closes)
                .map(price -> Math.pow(price - avgPrice, 2))
                .average().orElse(0.0));
            
            analysis.put("bbUpper", avgPrice + 2 * stdDev);
            analysis.put("bbMiddle", avgPrice);
            analysis.put("bbLower", avgPrice - 2 * stdDev);
            analysis.put("bbPosition", getBBPosition(currentPrice, new double[]{avgPrice + 2 * stdDev, avgPrice, avgPrice - 2 * stdDev}));
            
            // Volume analysis
            double avgVolume = Arrays.stream(volumes).average().orElse(0);
            double currentVolume = volumes[volumes.length - 1];
            analysis.put("volumeRatio", currentVolume / avgVolume);
            analysis.put("volumeSignal", getVolumeSignal(currentVolume, avgVolume));
            
            // Price momentum
            double priceChange = (closes[closes.length - 1] - closes[0]) / closes[0] * 100;
            analysis.put("priceChange", priceChange);
            analysis.put("momentum", getMomentumSignal(priceChange));
            
        } catch (Exception e) {
            System.err.println("Error calculating technical analysis: " + e.getMessage());
            // Fill with default values
            analysis.put("rsi", 50.0);
            analysis.put("macd", 0.0);
            analysis.put("ema20", 100.0);
            analysis.put("ema50", 100.0);
        }
        
        return analysis;
    }
    
    /**
     * Generate prediction signal based on analysis
     */
    private Map<String, Object> generatePredictionSignal(List<StockDataEntity> data, Map<String, Object> analysis) {
        Map<String, Object> signal = new HashMap<>();
        
        try {
            // Use PredictionEngine to generate signal
            List<stockprediction.model.StockData> stockDataList = data.stream()
                .map(entity -> new stockprediction.model.StockData(
                    entity.getTimestamp(),
                    entity.getOpen(),
                    entity.getHigh(),
                    entity.getLow(),
                    entity.getClose(),
                    entity.getVolume()
                ))
                .collect(Collectors.toList());
            
            List<stockprediction.model.PredictionSignal> predictions = predictionEngine.analyzeTrend(stockDataList);
            stockprediction.model.PredictionSignal prediction = predictions.isEmpty() ? null : predictions.get(0);
            
            if (prediction != null) {
                signal.put("signalType", prediction.getSignalType().toString());
                signal.put("confidence", prediction.getConfidence() * 100); // Convert to percentage
                signal.put("reasoning", prediction.getReason());
                signal.put("targetPrice", prediction.getPrice() * 1.1); // Mock target price
                signal.put("stopLoss", prediction.getPrice() * 0.9); // Mock stop loss
            } else {
                signal.put("signalType", "HOLD");
                signal.put("confidence", 50.0);
                signal.put("reasoning", "Insufficient data for analysis");
                signal.put("targetPrice", null);
                signal.put("stopLoss", null);
            }
            
            // Additional signal strength analysis
            double signalStrength = calculateSignalStrength(analysis);
            signal.put("signalStrength", signalStrength);
            signal.put("riskLevel", getRiskLevel(signalStrength));
            
        } catch (Exception e) {
            System.err.println("Error generating prediction signal: " + e.getMessage());
            // Generate mock signal
            signal.put("signalType", "HOLD");
            signal.put("confidence", 50.0);
            signal.put("reasoning", "Insufficient data for analysis");
        }
        
        return signal;
    }
    
    /**
     * Analyze market sentiment
     */
    private Map<String, Object> analyzeMarketSentiment(List<StockDataEntity> data) {
        Map<String, Object> sentiment = new HashMap<>();
        
        try {
            // Price trend analysis
            double[] closes = data.stream()
                .mapToDouble(StockDataEntity::getClose)
                .toArray();
            
            // Short-term trend (last 5 days)
            double shortTermTrend = (closes[closes.length - 1] - closes[Math.max(0, closes.length - 6)]) / closes[Math.max(0, closes.length - 6)] * 100;
            
            // Medium-term trend (last 20 days)
            double mediumTermTrend = (closes[closes.length - 1] - closes[Math.max(0, closes.length - 21)]) / closes[Math.max(0, closes.length - 21)] * 100;
            
            sentiment.put("shortTermTrend", shortTermTrend);
            sentiment.put("mediumTermTrend", mediumTermTrend);
            sentiment.put("overallSentiment", getOverallSentiment(shortTermTrend, mediumTermTrend));
            sentiment.put("volatility", calculateVolatility(closes));
            sentiment.put("trendStrength", getTrendStrength(shortTermTrend, mediumTermTrend));
            
        } catch (Exception e) {
            System.err.println("Error analyzing market sentiment: " + e.getMessage());
            sentiment.put("overallSentiment", "NEUTRAL");
        }
        
        return sentiment;
    }
    
    /**
     * Generate mock analysis when real data is not available
     */
    private Map<String, Object> generateMockAnalysis(String symbol) {
        Map<String, Object> result = new HashMap<>();
        
        // Mock technical analysis
        Map<String, Object> technicalAnalysis = new HashMap<>();
        double mockRsi = 45.0 + Math.random() * 20;
        technicalAnalysis.put("rsi", mockRsi);
        technicalAnalysis.put("rsiSignal", getRSISignal(mockRsi));
        
        double mockMacd = (Math.random() - 0.5) * 2;
        double mockSignal = mockMacd * 0.8;
        double mockHistogram = mockMacd * 0.2;
        technicalAnalysis.put("macd", mockMacd);
        technicalAnalysis.put("macdSignal", mockSignal);
        technicalAnalysis.put("macdHistogram", mockHistogram);
        technicalAnalysis.put("macdTrend", getMACDTrend(mockMacd, mockSignal, mockHistogram));
        
        double mockEma20 = 100.0 + Math.random() * 20;
        double mockEma50 = 95.0 + Math.random() * 15;
        technicalAnalysis.put("ema20", mockEma20);
        technicalAnalysis.put("ema50", mockEma50);
        technicalAnalysis.put("emaTrend", getEMATrend(mockEma20, mockEma50));
        
        technicalAnalysis.put("bbUpper", 110.0);
        technicalAnalysis.put("bbMiddle", 100.0);
        technicalAnalysis.put("bbLower", 90.0);
        technicalAnalysis.put("bbPosition", "WITHIN_BANDS");
        
        technicalAnalysis.put("volumeRatio", 1.0 + Math.random() * 0.5);
        technicalAnalysis.put("volumeSignal", "NORMAL_VOLUME");
        technicalAnalysis.put("priceChange", (Math.random() - 0.5) * 10);
        technicalAnalysis.put("momentum", "NEUTRAL");
        
        // Mock prediction signal
        Map<String, Object> predictionSignal = new HashMap<>();
        String[] signalTypes = {"LONG", "SHORT", "HOLD"};
        String signalType = signalTypes[(int) (Math.random() * signalTypes.length)];
        predictionSignal.put("signalType", signalType);
        predictionSignal.put("confidence", 60.0 + Math.random() * 30);
        predictionSignal.put("reasoning", "Mock analysis based on technical indicators");
        
        // Mock market sentiment
        Map<String, Object> marketSentiment = new HashMap<>();
        marketSentiment.put("overallSentiment", "BULLISH");
        marketSentiment.put("volatility", 0.15 + Math.random() * 0.1);
        
        result.put("symbol", symbol);
        result.put("timestamp", LocalDateTime.now());
        result.put("technicalAnalysis", technicalAnalysis);
        result.put("predictionSignal", predictionSignal);
        result.put("marketSentiment", marketSentiment);
        result.put("dataQuality", "mock");
        
        return result;
    }
    
    // Helper methods for signal interpretation
    private String getRSISignal(double rsi) {
        if (rsi > 70) return "OVERBOUGHT";
        if (rsi < 30) return "OVERSOLD";
        return "NEUTRAL";
    }
    
    private String getMACDTrend(double macdLine, double signalLine, double histogram) {
        if (macdLine > signalLine && histogram > 0) return "BULLISH";
        if (macdLine < signalLine && histogram < 0) return "BEARISH";
        return "NEUTRAL";
    }
    
    private String getEMATrend(double ema20, double ema50) {
        if (ema20 > ema50) return "BULLISH";
        if (ema20 < ema50) return "BEARISH";
        return "NEUTRAL";
    }
    
    private String getBBPosition(double price, double[] bb) {
        if (price > bb[0]) return "ABOVE_UPPER";
        if (price < bb[2]) return "BELOW_LOWER";
        return "WITHIN_BANDS";
    }
    
    private String getVolumeSignal(double current, double average) {
        if (current > average * 1.5) return "HIGH_VOLUME";
        if (current < average * 0.5) return "LOW_VOLUME";
        return "NORMAL_VOLUME";
    }
    
    private String getMomentumSignal(double change) {
        if (change > 5) return "STRONG_BULLISH";
        if (change > 1) return "BULLISH";
        if (change < -5) return "STRONG_BEARISH";
        if (change < -1) return "BEARISH";
        return "NEUTRAL";
    }
    
    private double calculateSignalStrength(Map<String, Object> analysis) {
        // Simple signal strength calculation
        double strength = 50.0; // Base strength
        
        // RSI contribution
        Object rsiObj = analysis.get("rsi");
        if (rsiObj instanceof Double) {
            double rsi = (Double) rsiObj;
            if (rsi > 70 || rsi < 30) strength += 20;
        }
        
        // MACD contribution
        Object macdObj = analysis.get("macd");
        if (macdObj instanceof Double) {
            double macd = (Double) macdObj;
            if (Math.abs(macd) > 1) strength += 15;
        }
        
        // EMA trend contribution
        Object emaTrendObj = analysis.get("emaTrend");
        if (emaTrendObj instanceof String) {
            String emaTrend = (String) emaTrendObj;
            if (!"NEUTRAL".equals(emaTrend)) strength += 15;
        }
        
        return Math.min(100.0, strength);
    }
    
    private String getRiskLevel(double strength) {
        if (strength > 80) return "LOW";
        if (strength > 60) return "MEDIUM";
        return "HIGH";
    }
    
    private String getOverallSentiment(double shortTerm, double mediumTerm) {
        if (shortTerm > 2 && mediumTerm > 2) return "STRONG_BULLISH";
        if (shortTerm > 0 && mediumTerm > 0) return "BULLISH";
        if (shortTerm < -2 && mediumTerm < -2) return "STRONG_BEARISH";
        if (shortTerm < 0 && mediumTerm < 0) return "BEARISH";
        return "NEUTRAL";
    }
    
    private double calculateVolatility(double[] prices) {
        if (prices.length < 2) return 0.0;
        
        double mean = Arrays.stream(prices).average().orElse(0.0);
        double variance = Arrays.stream(prices)
            .map(price -> Math.pow(price - mean, 2))
            .average()
            .orElse(0.0);
        
        return Math.sqrt(variance) / mean;
    }
    
    private String getTrendStrength(double shortTerm, double mediumTerm) {
        double avgTrend = (shortTerm + mediumTerm) / 2;
        if (Math.abs(avgTrend) > 5) return "STRONG";
        if (Math.abs(avgTrend) > 2) return "MODERATE";
        return "WEAK";
    }
}
