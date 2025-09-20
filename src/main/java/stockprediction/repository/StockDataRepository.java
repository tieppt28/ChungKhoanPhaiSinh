package stockprediction.repository;

import stockprediction.entity.StockDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for StockDataEntity
 */
@Repository
public interface StockDataRepository extends JpaRepository<StockDataEntity, Long> {
    
    /**
     * Find all stock data for a specific symbol
     */
    List<StockDataEntity> findBySymbolOrderByTimestampAsc(String symbol);
    
    /**
     * Find stock data for a symbol within a date range
     */
    @Query("SELECT s FROM StockDataEntity s WHERE s.symbol = :symbol " +
           "AND s.timestamp >= :startDate AND s.timestamp <= :endDate " +
           "ORDER BY s.timestamp ASC")
    List<StockDataEntity> findBySymbolAndTimestampBetween(
            @Param("symbol") String symbol,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find the latest stock data for a symbol
     */
    @Query("SELECT s FROM StockDataEntity s WHERE s.symbol = :symbol " +
           "ORDER BY s.timestamp DESC")
    List<StockDataEntity> findLatestBySymbol(@Param("symbol") String symbol);
    
    /**
     * Find all unique symbols
     */
    @Query("SELECT DISTINCT s.symbol FROM StockDataEntity s ORDER BY s.symbol")
    List<String> findAllSymbols();
    
    /**
     * Find stock data for multiple symbols
     */
    @Query("SELECT s FROM StockDataEntity s WHERE s.symbol IN :symbols " +
           "ORDER BY s.symbol, s.timestamp ASC")
    List<StockDataEntity> findBySymbolIn(@Param("symbols") List<String> symbols);
    
    /**
     * Count records for a symbol
     */
    long countBySymbol(String symbol);
    
    /**
     * Find stock data with limit
     */
    @Query("SELECT s FROM StockDataEntity s WHERE s.symbol = :symbol " +
           "ORDER BY s.timestamp DESC")
    List<StockDataEntity> findTopNBySymbolOrderByTimestampDesc(
            @Param("symbol") String symbol);
}
