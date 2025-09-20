package stockprediction.repository;

import stockprediction.entity.PredictionSignalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for PredictionSignalEntity
 */
@Repository
public interface PredictionSignalRepository extends JpaRepository<PredictionSignalEntity, Long> {
    
    /**
     * Find all prediction signals for a specific symbol
     */
    List<PredictionSignalEntity> findBySymbolOrderByTimestampDesc(String symbol);
    
    /**
     * Find prediction signals for a symbol within a date range
     */
    @Query("SELECT p FROM PredictionSignalEntity p WHERE p.symbol = :symbol " +
           "AND p.timestamp >= :startDate AND p.timestamp <= :endDate " +
           "ORDER BY p.timestamp DESC")
    List<PredictionSignalEntity> findBySymbolAndTimestampBetween(
            @Param("symbol") String symbol,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find the latest prediction signal for a symbol
     */
    @Query("SELECT p FROM PredictionSignalEntity p WHERE p.symbol = :symbol " +
           "ORDER BY p.timestamp DESC")
    List<PredictionSignalEntity> findLatestBySymbol(@Param("symbol") String symbol);
    
    /**
     * Find prediction signals by signal type
     */
    List<PredictionSignalEntity> findBySignalTypeOrderByTimestampDesc(
            PredictionSignalEntity.SignalType signalType);
    
    /**
     * Find prediction signals for a symbol by signal type
     */
    List<PredictionSignalEntity> findBySymbolAndSignalTypeOrderByTimestampDesc(
            String symbol, PredictionSignalEntity.SignalType signalType);
    
    /**
     * Find high confidence signals (confidence > threshold)
     */
    @Query("SELECT p FROM PredictionSignalEntity p WHERE p.confidence > :threshold " +
           "ORDER BY p.timestamp DESC")
    List<PredictionSignalEntity> findHighConfidenceSignals(@Param("threshold") Double threshold);
    
    /**
     * Find recent signals (within last N days)
     */
    @Query("SELECT p FROM PredictionSignalEntity p WHERE p.timestamp >= :since " +
           "ORDER BY p.timestamp DESC")
    List<PredictionSignalEntity> findRecentSignals(@Param("since") LocalDateTime since);
    
    /**
     * Count signals by type for a symbol
     */
    long countBySymbolAndSignalType(String symbol, PredictionSignalEntity.SignalType signalType);
}
