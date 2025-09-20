package stockprediction.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class for PredictionSignal stored in database
 */
@Entity
@Table(name = "prediction_signals")
public class PredictionSignalEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "symbol", nullable = false)
    private String symbol;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "signal_type", nullable = false)
    private SignalType signalType;
    
    @Column(name = "confidence", nullable = false)
    private Double confidence;
    
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    @Column(name = "price", nullable = false)
    private Double price;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum SignalType {
        LONG, SHORT, REVERSAL, HOLD
    }
    
    // Constructors
    public PredictionSignalEntity() {
        this.createdAt = LocalDateTime.now();
    }
    
    public PredictionSignalEntity(String symbol, LocalDateTime timestamp, SignalType signalType,
                                 Double confidence, String reason, Double price) {
        this();
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.signalType = signalType;
        this.confidence = confidence;
        this.reason = reason;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public SignalType getSignalType() { return signalType; }
    public void setSignalType(SignalType signalType) { this.signalType = signalType; }
    
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("PredictionSignalEntity{id=%d, symbol='%s', timestamp=%s, signalType=%s, confidence=%.2f%%, reason='%s', price=%.2f}",
                id, symbol, timestamp, signalType, confidence, reason, price);
    }
}
