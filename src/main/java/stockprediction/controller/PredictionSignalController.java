package stockprediction.controller;

import stockprediction.entity.PredictionSignalEntity;
import stockprediction.model.PredictionSignal;
import stockprediction.service.PredictionSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Prediction Signal API
 */
@RestController
@RequestMapping("/signals")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PredictionSignalController {
    
    @Autowired
    private PredictionSignalService predictionSignalService;
    
    /**
     * Get all prediction signals for a symbol
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<List<PredictionSignalEntity>> getSignalsBySymbol(@PathVariable String symbol) {
        List<PredictionSignalEntity> signals = predictionSignalService.getBySymbol(symbol);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get prediction signals for a symbol within date range
     */
    @GetMapping("/{symbol}/range")
    public ResponseEntity<List<PredictionSignalEntity>> getSignalsInRange(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<PredictionSignalEntity> signals = predictionSignalService.getBySymbolAndDateRange(symbol, startDate, endDate);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get latest prediction signal for a symbol
     */
    @GetMapping("/{symbol}/latest")
    public ResponseEntity<PredictionSignalEntity> getLatestSignal(@PathVariable String symbol) {
        PredictionSignalEntity signal = predictionSignalService.getLatestBySymbol(symbol);
        if (signal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signal);
    }
    
    /**
     * Get prediction signals by signal type
     */
    @GetMapping("/type/{signalType}")
    public ResponseEntity<List<PredictionSignalEntity>> getSignalsByType(
            @PathVariable PredictionSignalEntity.SignalType signalType) {
        List<PredictionSignalEntity> signals = predictionSignalService.getBySignalType(signalType);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get prediction signals for a symbol by signal type
     */
    @GetMapping("/{symbol}/type/{signalType}")
    public ResponseEntity<List<PredictionSignalEntity>> getSignalsBySymbolAndType(
            @PathVariable String symbol,
            @PathVariable PredictionSignalEntity.SignalType signalType) {
        List<PredictionSignalEntity> signals = predictionSignalService.getBySymbolAndSignalType(symbol, signalType);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get high confidence signals
     */
    @GetMapping("/high-confidence")
    public ResponseEntity<List<PredictionSignalEntity>> getHighConfidenceSignals(
            @RequestParam(defaultValue = "70.0") Double threshold) {
        List<PredictionSignalEntity> signals = predictionSignalService.getHighConfidenceSignals(threshold);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get recent signals
     */
    @GetMapping("/recent")
    public ResponseEntity<List<PredictionSignalEntity>> getRecentSignals(
            @RequestParam(defaultValue = "7") Integer days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<PredictionSignalEntity> signals = predictionSignalService.getRecentSignals(since);
        return ResponseEntity.ok(signals);
    }
    
    /**
     * Get signal statistics for a symbol
     */
    @GetMapping("/{symbol}/stats")
    public ResponseEntity<Map<String, Object>> getSignalStats(@PathVariable String symbol) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("symbol", symbol);
        stats.put("total", predictionSignalService.getBySymbol(symbol).size());
        stats.put("long", predictionSignalService.countBySymbolAndSignalType(symbol, PredictionSignalEntity.SignalType.LONG));
        stats.put("short", predictionSignalService.countBySymbolAndSignalType(symbol, PredictionSignalEntity.SignalType.SHORT));
        stats.put("reversal", predictionSignalService.countBySymbolAndSignalType(symbol, PredictionSignalEntity.SignalType.REVERSAL));
        stats.put("hold", predictionSignalService.countBySymbolAndSignalType(symbol, PredictionSignalEntity.SignalType.HOLD));
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Add new prediction signal
     */
    @PostMapping("/{symbol}")
    public ResponseEntity<PredictionSignalEntity> addSignal(
            @PathVariable String symbol,
            @RequestBody PredictionSignal signal) {
        
        PredictionSignalEntity entity = predictionSignalService.saveFromPredictionSignal(signal, symbol);
        return ResponseEntity.ok(entity);
    }
    
    /**
     * Add multiple prediction signals
     */
    @PostMapping("/{symbol}/batch")
    public ResponseEntity<List<PredictionSignalEntity>> addMultipleSignals(
            @PathVariable String symbol,
            @RequestBody List<PredictionSignal> signals) {
        
        List<PredictionSignalEntity> entities = predictionSignalService.saveFromPredictionSignalList(signals, symbol);
        return ResponseEntity.ok(entities);
    }
    
    /**
     * Delete all prediction signals for a symbol
     */
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Map<String, String>> deleteSignals(@PathVariable String symbol) {
        predictionSignalService.deleteBySymbol(symbol);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Prediction signals for " + symbol + " deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get signals for TradingView annotations
     */
    @GetMapping("/{symbol}/tradingview")
    public ResponseEntity<List<Map<String, Object>>> getSignalsForTradingView(@PathVariable String symbol) {
        List<PredictionSignalEntity> signals = predictionSignalService.getBySymbol(symbol);
        
        List<Map<String, Object>> tradingViewSignals = signals.stream()
            .map(signal -> {
                Map<String, Object> item = new HashMap<>();
                item.put("time", signal.getTimestamp().toLocalDate().toString());
                item.put("type", signal.getSignalType().toString().toLowerCase());
                item.put("confidence", signal.getConfidence());
                item.put("reason", signal.getReason());
                item.put("price", signal.getPrice());
                return item;
            })
            .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(tradingViewSignals);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Prediction Signal API");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
