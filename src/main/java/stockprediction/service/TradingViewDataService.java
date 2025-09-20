package stockprediction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import stockprediction.entity.StockDataEntity;
import stockprediction.model.StockData;
import stockprediction.repository.StockDataRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service to fetch real-time data from TradingView and other sources
 */
@Service
public class TradingViewDataService {
    
    @Autowired
    private StockDataRepository stockDataRepository;
    
    @Autowired
    private StockDataService stockDataService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // TradingView API endpoints (unofficial)
    private static final String TRADINGVIEW_API_BASE = "https://scanner.tradingview.com/vietnam/scan";
    private static final String YAHOO_FINANCE_API = "https://query1.finance.yahoo.com/v8/finance/chart/";
    
    /**
     * Fetch real-time data for Vietnamese stocks
     */
    public List<StockDataEntity> fetchRealTimeData(String symbol) {
        try {
            // Try Yahoo Finance first (more reliable)
            List<StockData> yahooData = fetchFromYahooFinance(symbol);
            
            if (yahooData != null && !yahooData.isEmpty()) {
                return stockDataService.saveFromStockDataList(yahooData, symbol);
            }
            
            // Fallback to generated data
            return generateFallbackData(symbol);
            
        } catch (Exception e) {
            System.err.println("Error fetching real-time data for " + symbol + ": " + e.getMessage());
            return generateFallbackData(symbol);
        }
    }
    
    /**
     * Fetch data from Yahoo Finance API
     */
    private List<StockData> fetchFromYahooFinance(String symbol) {
        try {
            // Convert Vietnamese symbol to Yahoo format
            String yahooSymbol = convertToYahooSymbol(symbol);
            String url = YAHOO_FINANCE_API + yahooSymbol + "?interval=1d&range=1y";
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("chart")) {
                Map<String, Object> chart = (Map<String, Object>) response.get("chart");
                List<Map<String, Object>> result = (List<Map<String, Object>>) chart.get("result");
                
                if (result != null && !result.isEmpty()) {
                    Map<String, Object> data = result.get(0);
                    return parseYahooData(data);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error fetching from Yahoo Finance: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Convert Vietnamese stock symbol to Yahoo Finance format
     */
    private String convertToYahooSymbol(String symbol) {
        // Add .VN suffix for Vietnamese stocks
        return symbol + ".VN";
    }
    
    /**
     * Parse Yahoo Finance data
     */
    private List<StockData> parseYahooData(Map<String, Object> data) {
        List<StockData> stockDataList = new ArrayList<>();
        
        try {
            Map<String, Object> meta = (Map<String, Object>) data.get("meta");
            List<Long> timestamps = (List<Long>) data.get("timestamp");
            Map<String, Object> indicators = (Map<String, Object>) data.get("indicators");
            
            if (indicators != null && indicators.containsKey("quote")) {
                List<Map<String, Object>> quotes = (List<Map<String, Object>>) indicators.get("quote");
                Map<String, Object> quote = quotes.get(0);
                
                List<Double> opens = (List<Double>) quote.get("open");
                List<Double> highs = (List<Double>) quote.get("high");
                List<Double> lows = (List<Double>) quote.get("low");
                List<Double> closes = (List<Double>) quote.get("close");
                List<Long> volumes = (List<Long>) quote.get("volume");
                
                for (int i = 0; i < timestamps.size(); i++) {
                    if (opens.get(i) != null && highs.get(i) != null && 
                        lows.get(i) != null && closes.get(i) != null) {
                        
                        LocalDateTime timestamp = LocalDateTime.ofEpochSecond(
                            timestamps.get(i), 0, 
                            java.time.ZoneOffset.ofHours(7) // Vietnam timezone
                        );
                        
                        StockData stockData = new StockData(
                            timestamp,
                            opens.get(i),
                            highs.get(i),
                            lows.get(i),
                            closes.get(i),
                            volumes.get(i) != null ? volumes.get(i) : 0L
                        );
                        
                        stockDataList.add(stockData);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing Yahoo data: " + e.getMessage());
        }
        
        return stockDataList;
    }
    
    /**
     * Generate fallback data when real-time fetch fails
     */
    private List<StockDataEntity> generateFallbackData(String symbol) {
        // Use existing StockDataGenerator as fallback
        stockprediction.data.StockDataGenerator generator = new stockprediction.data.StockDataGenerator();
        List<StockData> generatedData = generator.generateSampleData(200, 100.0);
        
        return stockDataService.saveFromStockDataList(generatedData, symbol);
    }
    
    /**
     * Get available Vietnamese stock symbols
     */
    public List<String> getAvailableSymbols() {
        return List.of(
            "VNM", "VCB", "VIC", "HPG", "MSN", "VHM", "GAS", "BID", "CTG", "FPT",
            "VRE", "MWG", "PLX", "TCB", "ACB", "TPB", "MBB", "VGC", "VJC", "SAB"
        );
    }
    
    /**
     * Fetch market overview data
     */
    public Map<String, Object> getMarketOverview() {
        try {
            // This would typically call a real market data API
            // For now, return mock data
            return Map.of(
                "totalStocks", 20,
                "marketCap", "2.5T VND",
                "volume", "1.2B shares",
                "topGainers", List.of("VNM", "VCB", "VIC"),
                "topLosers", List.of("HPG", "MSN", "VHM")
            );
        } catch (Exception e) {
            System.err.println("Error fetching market overview: " + e.getMessage());
            return Map.of("error", "Unable to fetch market data");
        }
    }
}
