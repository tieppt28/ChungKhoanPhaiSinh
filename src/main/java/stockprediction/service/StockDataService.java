package stockprediction.service;

import stockprediction.entity.StockDataEntity;
import stockprediction.model.StockData;
import stockprediction.repository.StockDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing stock data
 */
@Service
@Transactional
public class StockDataService {
    
    @Autowired
    private StockDataRepository stockDataRepository;
    
    /**
     * Save a single stock data entity
     */
    public StockDataEntity save(StockDataEntity stockData) {
        return stockDataRepository.save(stockData);
    }
    
    /**
     * Save multiple stock data entities
     */
    public List<StockDataEntity> saveAll(List<StockDataEntity> stockDataList) {
        return stockDataRepository.saveAll(stockDataList);
    }
    
    /**
     * Convert StockData to StockDataEntity and save
     */
    public StockDataEntity saveFromStockData(StockData stockData, String symbol) {
        StockDataEntity entity = new StockDataEntity(
            symbol,
            stockData.getTimestamp(),
            stockData.getOpen(),
            stockData.getHigh(),
            stockData.getLow(),
            stockData.getClose(),
            stockData.getVolume()
        );
        return save(entity);
    }
    
    /**
     * Convert and save multiple StockData objects
     */
    public List<StockDataEntity> saveFromStockDataList(List<StockData> stockDataList, String symbol) {
        List<StockDataEntity> entities = stockDataList.stream()
            .map(data -> new StockDataEntity(
                symbol,
                data.getTimestamp(),
                data.getOpen(),
                data.getHigh(),
                data.getLow(),
                data.getClose(),
                data.getVolume()
            ))
            .collect(Collectors.toList());
        return saveAll(entities);
    }
    
    /**
     * Get all stock data for a symbol
     */
    @Transactional(readOnly = true)
    public List<StockDataEntity> getBySymbol(String symbol) {
        return stockDataRepository.findBySymbolOrderByTimestampAsc(symbol);
    }
    
    /**
     * Get stock data for a symbol within date range
     */
    @Transactional(readOnly = true)
    public List<StockDataEntity> getBySymbolAndDateRange(String symbol, 
                                                        LocalDateTime startDate, 
                                                        LocalDateTime endDate) {
        return stockDataRepository.findBySymbolAndTimestampBetween(symbol, startDate, endDate);
    }
    
    /**
     * Get latest stock data for a symbol
     */
    @Transactional(readOnly = true)
    public StockDataEntity getLatestBySymbol(String symbol) {
        List<StockDataEntity> latest = stockDataRepository.findLatestBySymbol(symbol);
        return latest.isEmpty() ? null : latest.get(0);
    }
    
    /**
     * Get all available symbols
     */
    @Transactional(readOnly = true)
    public List<String> getAllSymbols() {
        return stockDataRepository.findAllSymbols();
    }
    
    /**
     * Get stock data for multiple symbols
     */
    @Transactional(readOnly = true)
    public List<StockDataEntity> getBySymbols(List<String> symbols) {
        return stockDataRepository.findBySymbolIn(symbols);
    }
    
    /**
     * Count records for a symbol
     */
    @Transactional(readOnly = true)
    public long countBySymbol(String symbol) {
        return stockDataRepository.countBySymbol(symbol);
    }
    
    /**
     * Convert StockDataEntity to StockData
     */
    public StockData convertToStockData(StockDataEntity entity) {
        return new StockData(
            entity.getTimestamp(),
            entity.getOpen(),
            entity.getHigh(),
            entity.getLow(),
            entity.getClose(),
            entity.getVolume()
        );
    }
    
    /**
     * Convert list of StockDataEntity to StockData
     */
    public List<StockData> convertToStockDataList(List<StockDataEntity> entities) {
        return entities.stream()
            .map(this::convertToStockData)
            .collect(Collectors.toList());
    }
    
    /**
     * Delete all data for a symbol
     */
    public void deleteBySymbol(String symbol) {
        List<StockDataEntity> entities = getBySymbol(symbol);
        stockDataRepository.deleteAll(entities);
    }
    
    /**
     * Check if symbol exists
     */
    @Transactional(readOnly = true)
    public boolean symbolExists(String symbol) {
        return countBySymbol(symbol) > 0;
    }
}
