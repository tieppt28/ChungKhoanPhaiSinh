package stockprediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Spring Boot Application for Stock Prediction System
 */
@SpringBootApplication
public class StockPredictionApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockPredictionApplication.class, args);
        System.out.println("\n=== STOCK PREDICTION SYSTEM STARTED ===");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("H2 Console: http://localhost:8080/api/h2-console");
        System.out.println("Available endpoints:");
        System.out.println("  GET /api/stocks/symbols - Get all stock symbols");
        System.out.println("  GET /api/stocks/{symbol} - Get stock data for symbol");
        System.out.println("  GET /api/stocks/{symbol}/tradingview - Get data in TradingView format");
        System.out.println("  GET /api/signals/{symbol} - Get prediction signals for symbol");
        System.out.println("  GET /api/signals/{symbol}/tradingview - Get signals in TradingView format");
        System.out.println("==========================================\n");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

