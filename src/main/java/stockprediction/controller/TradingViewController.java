package stockprediction.controller;

import stockprediction.service.TradingViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for TradingView integration
 */
@RestController
@RequestMapping("/tradingview")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class TradingViewController {
    
    @Autowired
    private TradingViewDataService tradingViewDataService;
    
    /**
     * Get available Vietnamese stock symbols
     */
    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getAvailableSymbols() {
        List<String> symbols = tradingViewDataService.getAvailableSymbols();
        return ResponseEntity.ok(symbols);
    }
    
    /**
     * Fetch real-time data for a symbol
     */
    @GetMapping("/data/{symbol}")
    public ResponseEntity<Map<String, Object>> getRealTimeData(@PathVariable String symbol) {
        try {
            List<stockprediction.entity.StockDataEntity> data = 
                tradingViewDataService.fetchRealTimeData(symbol);
            
            Map<String, Object> response = Map.of(
                "symbol", symbol,
                "data", data,
                "count", data.size(),
                "source", "real-time"
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to fetch data for " + symbol, "message", e.getMessage())
            );
        }
    }
    
    /**
     * Get market overview
     */
    @GetMapping("/market-overview")
    public ResponseEntity<Map<String, Object>> getMarketOverview() {
        Map<String, Object> overview = tradingViewDataService.getMarketOverview();
        return ResponseEntity.ok(overview);
    }
    
    /**
     * Get TradingView widget configuration
     */
    @GetMapping("/widget-config/{symbol}")
    public ResponseEntity<Map<String, Object>> getWidgetConfig(@PathVariable String symbol) {
        Map<String, Object> config = Map.of(
            "symbol", "HOSE:" + symbol,
            "interval", "1D",
            "timezone", "Asia/Ho_Chi_Minh",
            "theme", "dark",
            "locale", "vi",
            "studies", List.of(
                "RSI@tv-basicstudies",
                "MACD@tv-basicstudies",
                "EMA@tv-basicstudies"
            ),
            "indicators", List.of(
                Map.of(
                    "id", "RSI",
                    "inputs", Map.of("length", 14)
                ),
                Map.of(
                    "id", "MACD",
                    "inputs", Map.of(
                        "fastLength", 12,
                        "slowLength", 26,
                        "signalLength", 9
                    )
                )
            )
        );
        
        return ResponseEntity.ok(config);
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "TradingView Integration API",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}

