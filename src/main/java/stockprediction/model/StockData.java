package stockprediction.model;


import java.time.LocalDateTime;

/**
 * Represents a single stock data point (candlestick)
 */
public class StockData {
    private LocalDateTime timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public StockData(LocalDateTime timestamp, double open, double high, double low, double close, long volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public double getOpen() { return open; }
    public void setOpen(double open) { this.open = open; }

    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }

    public double getLow() { return low; }
    public void setLow(double low) { this.low = low; }

    public double getClose() { return close; }
    public void setClose(double close) { this.close = close; }

    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }

    @Override
    public String toString() {
        return String.format("StockData{timestamp=%s, open=%.2f, high=%.2f, low=%.2f, close=%.2f, volume=%d}",
                timestamp, open, high, low, close, volume);
    }
}
