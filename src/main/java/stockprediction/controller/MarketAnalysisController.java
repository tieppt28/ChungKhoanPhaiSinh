package stockprediction.controller;

import stockprediction.service.MarketAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Market Analysis
 */
@RestController
@RequestMapping("/market-analysis")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class MarketAnalysisController {
    
    @Autowired
    private MarketAnalysisService marketAnalysisService;
    
    /**
     * Get comprehensive market analysis for a symbol
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<Map<String, Object>> getMarketAnalysis(@PathVariable String symbol) {
        try {
            Map<String, Object> analysis = marketAnalysisService.getMarketAnalysis(symbol);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to analyze market for " + symbol, "message", e.getMessage())
            );
        }
    }
    
    /**
     * Get technical analysis only
     */
    @GetMapping("/{symbol}/technical")
    public ResponseEntity<Map<String, Object>> getTechnicalAnalysis(@PathVariable String symbol) {
        try {
            Map<String, Object> fullAnalysis = marketAnalysisService.getMarketAnalysis(symbol);
            Map<String, Object> technicalAnalysis = (Map<String, Object>) fullAnalysis.get("technicalAnalysis");
            return ResponseEntity.ok(technicalAnalysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get technical analysis for " + symbol, "message", e.getMessage())
            );
        }
    }
    
    /**
     * Get prediction signal only
     */
    @GetMapping("/{symbol}/signal")
    public ResponseEntity<Map<String, Object>> getPredictionSignal(@PathVariable String symbol) {
        try {
            Map<String, Object> fullAnalysis = marketAnalysisService.getMarketAnalysis(symbol);
            Map<String, Object> predictionSignal = (Map<String, Object>) fullAnalysis.get("predictionSignal");
            return ResponseEntity.ok(predictionSignal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get prediction signal for " + symbol, "message", e.getMessage())
            );
        }
    }
    
    /**
     * Get market sentiment only
     */
    @GetMapping("/{symbol}/sentiment")
    public ResponseEntity<Map<String, Object>> getMarketSentiment(@PathVariable String symbol) {
        try {
            Map<String, Object> fullAnalysis = marketAnalysisService.getMarketAnalysis(symbol);
            Map<String, Object> marketSentiment = (Map<String, Object>) fullAnalysis.get("marketSentiment");
            return ResponseEntity.ok(marketSentiment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get market sentiment for " + symbol, "message", e.getMessage())
            );
        }
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Market Analysis API",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}

