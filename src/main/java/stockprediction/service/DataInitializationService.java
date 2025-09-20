package stockprediction.service;

import stockprediction.data.StockDataGenerator;
import stockprediction.entity.StockDataEntity;
import stockprediction.model.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Service to initialize sample data when application starts
 */
@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private StockDataService stockDataService;
    
    @Autowired
    private PredictionSignalService predictionSignalService;
    
    // Sample stock symbols
    private static final List<String> SAMPLE_SYMBOLS = Arrays.asList(
        "VNM", "VCB", "VIC", "HPG", "MSN", "VHM", "GAS", "BID", "CTG", "FPT"
    );
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INITIALIZING SAMPLE STOCK DATA ===");
        
        // Generate sample data for each symbol
        for (String symbol : SAMPLE_SYMBOLS) {
            generateSampleDataForSymbol(symbol);
        }
        
        System.out.println("=== SAMPLE DATA INITIALIZATION COMPLETED ===");
    }
    
    private void generateSampleDataForSymbol(String symbol) {
        try {
            // Check if data already exists
            if (stockDataService.symbolExists(symbol)) {
                System.out.println("Data for " + symbol + " already exists, skipping...");
                return;
            }
            
            // Generate different patterns for different symbols
            List<StockData> stockDataList;
            String pattern = getPatternForSymbol(symbol);
            
            switch (pattern) {
                case "bullish":
                    stockDataList = StockDataGenerator.generatePatternData(200, 100.0, "bullish");
                    break;
                case "bearish":
                    stockDataList = StockDataGenerator.generatePatternData(200, 150.0, "bearish");
                    break;
                case "sideways":
                    stockDataList = StockDataGenerator.generatePatternData(200, 120.0, "sideways");
                    break;
                case "volatile":
                    stockDataList = StockDataGenerator.generatePatternData(200, 80.0, "volatile");
                    break;
                default:
                    stockDataList = StockDataGenerator.generateSampleData(200, 100.0);
            }
            
            // Save to database
            List<StockDataEntity> entities = stockDataService.saveFromStockDataList(stockDataList, symbol);
            
            System.out.println("Generated " + entities.size() + " data points for " + symbol + 
                             " (pattern: " + pattern + ")");
            
            // Generate prediction signals
            generatePredictionSignals(symbol, stockDataList);
            
        } catch (Exception e) {
            System.err.println("Error generating data for " + symbol + ": " + e.getMessage());
        }
    }
    
    private String getPatternForSymbol(String symbol) {
        // Assign different patterns to different symbols for variety
        switch (symbol) {
            case "VNM":
            case "VCB":
            case "VIC":
                return "bullish";
            case "HPG":
            case "MSN":
            case "VHM":
                return "bearish";
            case "GAS":
            case "BID":
                return "sideways";
            case "CTG":
            case "FPT":
                return "volatile";
            default:
                return "random";
        }
    }
    
    private void generatePredictionSignals(String symbol, List<StockData> stockDataList) {
        try {
            // Use existing prediction engine to generate signals
            stockprediction.engine.PredictionEngine engine = 
                new stockprediction.engine.PredictionEngine();
            
            List<stockprediction.model.PredictionSignal> signals = 
                engine.analyzeTrend(stockDataList);
            
            // Convert and save signals
            for (stockprediction.model.PredictionSignal signal : signals) {
                predictionSignalService.saveFromPredictionSignal(signal, symbol);
            }
            
            System.out.println("Generated " + signals.size() + " prediction signals for " + symbol);
            
        } catch (Exception e) {
            System.err.println("Error generating prediction signals for " + symbol + ": " + e.getMessage());
        }
    }
}
