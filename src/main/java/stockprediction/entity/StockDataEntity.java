package stockprediction.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class for StockData stored in database
 */
@Entity
@Table(name = "stock_data")
public class StockDataEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "symbol", nullable = false)
    private String symbol;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "open_price", nullable = false)
    private Double open;
    
    @Column(name = "high_price", nullable = false)
    private Double high;
    
    @Column(name = "low_price", nullable = false)
    private Double low;
    
    @Column(name = "close_price", nullable = false)
    private Double close;
    
    @Column(name = "volume", nullable = false)
    private Long volume;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public StockDataEntity() {
        this.createdAt = LocalDateTime.now();
    }
    
    public StockDataEntity(String symbol, LocalDateTime timestamp, Double open, 
                          Double high, Double low, Double close, Long volume) {
        this();
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Double getOpen() { return open; }
    public void setOpen(Double open) { this.open = open; }
    
    public Double getHigh() { return high; }
    public void setHigh(Double high) { this.high = high; }
    
    public Double getLow() { return low; }
    public void setLow(Double low) { this.low = low; }
    
    public Double getClose() { return close; }
    public void setClose(Double close) { this.close = close; }
    
    public Long getVolume() { return volume; }
    public void setVolume(Long volume) { this.volume = volume; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("StockDataEntity{id=%d, symbol='%s', timestamp=%s, open=%.2f, high=%.2f, low=%.2f, close=%.2f, volume=%d}",
                id, symbol, timestamp, open, high, low, close, volume);
    }
}
