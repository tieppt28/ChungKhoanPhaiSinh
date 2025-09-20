package stockprediction.data;

import stockprediction.model.StockData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates sample stock data for testing and demonstration
 * In a real application, this would be replaced with actual market data API
 */
public class StockDataGenerator {

    private static final Random random = new Random();

    private static final String[] STOCK_SYMBOLS = {
            "VNM", "VIC", "VHM", "VCB", "BID", "CTG", "HPG", "MSN", "SAB", "GAS",
            "PLX", "POW", "REE", "VJC", "HDB", "TPB", "MBB", "STB", "TCB", "ACB"
    };

    private static final String[] COMPANY_NAMES = {
            "Vinamilk", "Vingroup", "Vinhomes", "Vietcombank", "BIDV", "Vietinbank",
            "Hoa Phat Group", "Masan Group", "Sabeco", "PetroVietnam Gas",
            "Petrolimex", "PetroVietnam Power", "REE Corporation", "VietJet Air",
            "HDBank", "TPBank", "Military Bank", "Sacombank", "Techcombank", "ACB"
    };

    /**
     * Generate sample stock data with realistic price movements
     */
    public static List<StockData> generateSampleData(int numberOfDays, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            // Generate realistic OHLC data
            double volatility = 0.02; // 2% daily volatility
            double change = (random.nextGaussian() * volatility * currentPrice);

            double open = currentPrice;
            double close = currentPrice + change;

            // Ensure high is the highest and low is the lowest
            double high = Math.max(open, close) + (random.nextDouble() * 0.01 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.01 * currentPrice);

            // Generate volume (random between 100K and 1M)
            long volume = 100000 + random.nextInt(900000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    /**
     * Generate trending stock data (upward trend)
     */
    public static List<StockData> generateTrendingData(int numberOfDays, double startPrice, double trendStrength) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            // Add trend component
            double trendComponent = trendStrength * currentPrice / numberOfDays;
            double volatility = 0.015; // Slightly lower volatility for trending data
            double change = trendComponent + (random.nextGaussian() * volatility * currentPrice);

            double open = currentPrice;
            double close = currentPrice + change;

            double high = Math.max(open, close) + (random.nextDouble() * 0.008 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.008 * currentPrice);

            long volume = 150000 + random.nextInt(800000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    /**
     * Generate stock data with specific patterns for testing
     */
    public static List<StockData> generatePatternData(int numberOfDays, double startPrice, String pattern) {
        switch (pattern.toLowerCase()) {
            case "bullish":
                return generateTrendingData(numberOfDays, startPrice, 0.1); // 10% upward trend
            case "bearish":
                return generateTrendingData(numberOfDays, startPrice, -0.1); // 10% downward trend
            case "sideways":
                return generateSidewaysData(numberOfDays, startPrice);
            case "volatile":
                return generateVolatileData(numberOfDays, startPrice);
            default:
                return generateSampleData(numberOfDays, startPrice);
        }
    }

    private static List<StockData> generateSidewaysData(int numberOfDays, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double basePrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            // Oscillate around base price
            double oscillation = Math.sin(i * 0.1) * 0.02 * basePrice;
            double noise = random.nextGaussian() * 0.01 * basePrice;

            double open = basePrice + oscillation;
            double close = open + noise;

            double high = Math.max(open, close) + (random.nextDouble() * 0.005 * basePrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.005 * basePrice);

            long volume = 120000 + random.nextInt(600000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    private static List<StockData> generateVolatileData(int numberOfDays, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            // High volatility
            double volatility = 0.04; // 4% daily volatility
            double change = random.nextGaussian() * volatility * currentPrice;

            double open = currentPrice;
            double close = currentPrice + change;

            double high = Math.max(open, close) + (random.nextDouble() * 0.02 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.02 * currentPrice);

            long volume = 200000 + random.nextInt(1000000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    /**
     * Generate data for multiple stocks with different patterns
     */
    public static List<List<StockData>> generateMultipleStocksData(int numberOfStocks, int numberOfDays) {
        List<List<StockData>> allStocksData = new ArrayList<>();

        for (int i = 0; i < numberOfStocks && i < STOCK_SYMBOLS.length; i++) {
            double startPrice = 10 + random.nextDouble() * 190; // Price between 10-200
            String pattern = getRandomPattern();
            List<StockData> stockData = generatePatternData(numberOfDays, startPrice, pattern);
            allStocksData.add(stockData);
        }

        return allStocksData;
    }

    /**
     * Generate intraday stock data (hourly intervals)
     */
    public static List<StockData> generateIntradayData(int numberOfHours, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusHours(numberOfHours);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfHours; i++) {
            // Lower volatility for hourly data
            double volatility = 0.005; // 0.5% hourly volatility
            double change = random.nextGaussian() * volatility * currentPrice;

            double open = currentPrice;
            double close = currentPrice + change;

            double high = Math.max(open, close) + (random.nextDouble() * 0.002 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.002 * currentPrice);

            // Lower volume for hourly data
            long volume = 5000 + random.nextInt(50000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusHours(1);
        }

        return stockData;
    }

    /**
     * Generate stock data with price gaps (gap up/down scenarios)
     */
    public static List<StockData> generateDataWithGaps(int numberOfDays, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            double open = currentPrice;

            // 10% chance of gap
            if (random.nextDouble() < 0.1) {
                double gapPercent = (random.nextDouble() - 0.5) * 0.1; // ±5% gap
                open = currentPrice * (1 + gapPercent);
            }

            double volatility = 0.02;
            double change = random.nextGaussian() * volatility * open;
            double close = open + change;

            double high = Math.max(open, close) + (random.nextDouble() * 0.01 * open);
            double low = Math.min(open, close) - (random.nextDouble() * 0.01 * open);

            long volume = 100000 + random.nextInt(900000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    /**
     * Generate stock data with clear support and resistance levels
     */
    public static List<StockData> generateDataWithSupportResistance(int numberOfDays, double startPrice) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        // Define support and resistance levels
        double supportLevel = startPrice * 0.9;
        double resistanceLevel = startPrice * 1.1;

        for (int i = 0; i < numberOfDays; i++) {
            double volatility = 0.015;
            double change = random.nextGaussian() * volatility * currentPrice;

            double open = currentPrice;
            double close = currentPrice + change;

            // Apply support/resistance logic
            if (close < supportLevel && random.nextDouble() < 0.7) {
                close = supportLevel + (random.nextDouble() * 0.02 * supportLevel);
            } else if (close > resistanceLevel && random.nextDouble() < 0.7) {
                close = resistanceLevel - (random.nextDouble() * 0.02 * resistanceLevel);
            }

            double high = Math.max(open, close) + (random.nextDouble() * 0.008 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.008 * currentPrice);

            // Higher volume near support/resistance
            long baseVolume = 150000;
            if (Math.abs(close - supportLevel) < 0.02 * supportLevel ||
                    Math.abs(close - resistanceLevel) < 0.02 * resistanceLevel) {
                baseVolume *= 1.5;
            }
            long volume = baseVolume + random.nextInt(500000);

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    /**
     * Generate data for extreme market conditions (crash, bubble, etc.)
     */
    public static List<StockData> generateExtremeMarketData(int numberOfDays, double startPrice, String condition) {
        List<StockData> stockData = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusDays(numberOfDays);
        double currentPrice = startPrice;

        for (int i = 0; i < numberOfDays; i++) {
            double change = 0;
            long volumeMultiplier = 1;

            switch (condition.toLowerCase()) {
                case "crash":
                    // Severe downward trend with high volatility
                    change = -0.05 * currentPrice + (random.nextGaussian() * 0.04 * currentPrice);
                    volumeMultiplier = 3;
                    break;
                case "bubble":
                    // Rapid upward trend with increasing volatility
                    double bubbleStrength = Math.min(2.0, 1.0 + (double)i / numberOfDays);
                    change = 0.03 * currentPrice * bubbleStrength + (random.nextGaussian() * 0.03 * currentPrice);
                    volumeMultiplier = 2;
                    break;
                case "flash_crash":
                    // Normal until sudden drop
                    if (i == numberOfDays / 2) {
                        change = -0.2 * currentPrice; // 20% drop
                        volumeMultiplier = 5;
                    } else {
                        change = random.nextGaussian() * 0.01 * currentPrice;
                    }
                    break;
                default:
                    change = random.nextGaussian() * 0.02 * currentPrice;
            }

            double open = currentPrice;
            double close = Math.max(0.1, currentPrice + change); // Prevent negative prices

            double high = Math.max(open, close) + (random.nextDouble() * 0.01 * currentPrice);
            double low = Math.min(open, close) - (random.nextDouble() * 0.01 * currentPrice);
            low = Math.max(0.1, low); // Prevent negative prices

            long volume = (150000 + random.nextInt(800000)) * volumeMultiplier;

            stockData.add(new StockData(currentTime, open, high, low, close, volume));

            currentPrice = close;
            currentTime = currentTime.plusDays(1);
        }

        return stockData;
    }

    private static String getRandomPattern() {
        String[] patterns = {"bullish", "bearish", "sideways", "volatile"};
        return patterns[random.nextInt(patterns.length)];
    }

    public static String getStockSymbol(int index) {
        return index < STOCK_SYMBOLS.length ? STOCK_SYMBOLS[index] : "STOCK" + index;
    }

    public static String getCompanyName(int index) {
        return index < COMPANY_NAMES.length ? COMPANY_NAMES[index] : "Company " + index;
    }
}
