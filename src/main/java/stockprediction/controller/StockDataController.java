package stockprediction.controller;

import stockprediction.entity.StockDataEntity;
import stockprediction.model.StockData;
import stockprediction.service.StockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Stock Data API
 */
@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class StockDataController {
    
    @Autowired
    private StockDataService stockDataService;
    
    /**
     * Get all available stock symbols
     */
    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getAllSymbols() {
        List<String> symbols = stockDataService.getAllSymbols();
        return ResponseEntity.ok(symbols);
    }
    
    /**
     * Get stock data for a specific symbol
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<List<StockDataEntity>> getStockData(@PathVariable String symbol) {
        List<StockDataEntity> data = stockDataService.getBySymbol(symbol);
        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
    
    /**
     * Get stock data for a symbol within date range
     */
    @GetMapping("/{symbol}/range")
    public ResponseEntity<List<StockDataEntity>> getStockDataInRange(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<StockDataEntity> data = stockDataService.getBySymbolAndDateRange(symbol, startDate, endDate);
        return ResponseEntity.ok(data);
    }
    
    /**
     * Get latest stock data for a symbol
     */
    @GetMapping("/{symbol}/latest")
    public ResponseEntity<StockDataEntity> getLatestStockData(@PathVariable String symbol) {
        StockDataEntity data = stockDataService.getLatestBySymbol(symbol);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
    
    /**
     * Get stock data for multiple symbols
     */
    @PostMapping("/multiple")
    public ResponseEntity<List<StockDataEntity>> getMultipleStockData(@RequestBody List<String> symbols) {
        List<StockDataEntity> data = stockDataService.getBySymbols(symbols);
        return ResponseEntity.ok(data);
    }
    
    /**
     * Get stock data count for a symbol
     */
    @GetMapping("/{symbol}/count")
    public ResponseEntity<Map<String, Object>> getStockDataCount(@PathVariable String symbol) {
        long count = stockDataService.countBySymbol(symbol);
        Map<String, Object> response = new HashMap<>();
        response.put("symbol", symbol);
        response.put("count", count);
        response.put("exists", count > 0);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Add new stock data for a symbol
     */
    @PostMapping("/{symbol}")
    public ResponseEntity<StockDataEntity> addStockData(
            @PathVariable String symbol,
            @RequestBody StockData stockData) {
        
        StockDataEntity entity = stockDataService.saveFromStockData(stockData, symbol);
        return ResponseEntity.ok(entity);
    }
    
    /**
     * Add multiple stock data for a symbol
     */
    @PostMapping("/{symbol}/batch")
    public ResponseEntity<List<StockDataEntity>> addMultipleStockData(
            @PathVariable String symbol,
            @RequestBody List<StockData> stockDataList) {
        
        List<StockDataEntity> entities = stockDataService.saveFromStockDataList(stockDataList, symbol);
        return ResponseEntity.ok(entities);
    }
    
    /**
     * Delete all stock data for a symbol
     */
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Map<String, String>> deleteStockData(@PathVariable String symbol) {
        stockDataService.deleteBySymbol(symbol);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Stock data for " + symbol + " deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get stock data in TradingView format
     */
    @GetMapping("/{symbol}/tradingview")
    public ResponseEntity<List<Map<String, Object>>> getStockDataForTradingView(@PathVariable String symbol) {
        List<StockDataEntity> data = stockDataService.getBySymbol(symbol);
        
        List<Map<String, Object>> tradingViewData = data.stream()
            .map(entity -> {
                Map<String, Object> item = new HashMap<>();
                item.put("time", entity.getTimestamp().toLocalDate().toString());
                item.put("open", entity.getOpen());
                item.put("high", entity.getHigh());
                item.put("low", entity.getLow());
                item.put("close", entity.getClose());
                item.put("volume", entity.getVolume());
                return item;
            })
            .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(tradingViewData);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Stock Data API");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
