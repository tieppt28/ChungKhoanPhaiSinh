package stockprediction.service;

import stockprediction.entity.PredictionSignalEntity;
import stockprediction.model.PredictionSignal;
import stockprediction.repository.PredictionSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing prediction signals
 */
@Service
@Transactional
public class PredictionSignalService {
    
    @Autowired
    private PredictionSignalRepository predictionSignalRepository;
    
    /**
     * Save a single prediction signal entity
     */
    public PredictionSignalEntity save(PredictionSignalEntity signal) {
        return predictionSignalRepository.save(signal);
    }
    
    /**
     * Save multiple prediction signal entities
     */
    public List<PredictionSignalEntity> saveAll(List<PredictionSignalEntity> signals) {
        return predictionSignalRepository.saveAll(signals);
    }
    
    /**
     * Convert PredictionSignal to PredictionSignalEntity and save
     */
    public PredictionSignalEntity saveFromPredictionSignal(PredictionSignal signal, String symbol) {
        PredictionSignalEntity entity = new PredictionSignalEntity(
            symbol,
            signal.getTimestamp(),
            convertSignalType(signal.getSignalType()),
            signal.getConfidence(),
            signal.getReason(),
            signal.getPrice()
        );
        return save(entity);
    }
    
    /**
     * Convert and save multiple PredictionSignal objects
     */
    public List<PredictionSignalEntity> saveFromPredictionSignalList(List<PredictionSignal> signals, String symbol) {
        List<PredictionSignalEntity> entities = signals.stream()
            .map(signal -> new PredictionSignalEntity(
                symbol,
                signal.getTimestamp(),
                convertSignalType(signal.getSignalType()),
                signal.getConfidence(),
                signal.getReason(),
                signal.getPrice()
            ))
            .collect(Collectors.toList());
        return saveAll(entities);
    }
    
    /**
     * Get all prediction signals for a symbol
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getBySymbol(String symbol) {
        return predictionSignalRepository.findBySymbolOrderByTimestampDesc(symbol);
    }
    
    /**
     * Get prediction signals for a symbol within date range
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getBySymbolAndDateRange(String symbol, 
                                                              LocalDateTime startDate, 
                                                              LocalDateTime endDate) {
        return predictionSignalRepository.findBySymbolAndTimestampBetween(symbol, startDate, endDate);
    }
    
    /**
     * Get latest prediction signal for a symbol
     */
    @Transactional(readOnly = true)
    public PredictionSignalEntity getLatestBySymbol(String symbol) {
        List<PredictionSignalEntity> latest = predictionSignalRepository.findLatestBySymbol(symbol);
        return latest.isEmpty() ? null : latest.get(0);
    }
    
    /**
     * Get prediction signals by signal type
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getBySignalType(PredictionSignalEntity.SignalType signalType) {
        return predictionSignalRepository.findBySignalTypeOrderByTimestampDesc(signalType);
    }
    
    /**
     * Get prediction signals for a symbol by signal type
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getBySymbolAndSignalType(String symbol, 
                                                               PredictionSignalEntity.SignalType signalType) {
        return predictionSignalRepository.findBySymbolAndSignalTypeOrderByTimestampDesc(symbol, signalType);
    }
    
    /**
     * Get high confidence signals
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getHighConfidenceSignals(Double threshold) {
        return predictionSignalRepository.findHighConfidenceSignals(threshold);
    }
    
    /**
     * Get recent signals
     */
    @Transactional(readOnly = true)
    public List<PredictionSignalEntity> getRecentSignals(LocalDateTime since) {
        return predictionSignalRepository.findRecentSignals(since);
    }
    
    /**
     * Count signals by type for a symbol
     */
    @Transactional(readOnly = true)
    public long countBySymbolAndSignalType(String symbol, PredictionSignalEntity.SignalType signalType) {
        return predictionSignalRepository.countBySymbolAndSignalType(symbol, signalType);
    }
    
    /**
     * Convert PredictionSignal.SignalType to PredictionSignalEntity.SignalType
     */
    private PredictionSignalEntity.SignalType convertSignalType(PredictionSignal.SignalType signalType) {
        switch (signalType) {
            case LONG:
                return PredictionSignalEntity.SignalType.LONG;
            case SHORT:
                return PredictionSignalEntity.SignalType.SHORT;
            case REVERSAL:
                return PredictionSignalEntity.SignalType.REVERSAL;
            default:
                return PredictionSignalEntity.SignalType.HOLD;
        }
    }
    
    /**
     * Convert PredictionSignalEntity to PredictionSignal
     */
    public PredictionSignal convertToPredictionSignal(PredictionSignalEntity entity) {
        return new PredictionSignal(
            entity.getTimestamp(),
            convertToModelSignalType(entity.getSignalType()),
            entity.getConfidence(),
            entity.getReason(),
            entity.getPrice()
        );
    }
    
    /**
     * Convert list of PredictionSignalEntity to PredictionSignal
     */
    public List<PredictionSignal> convertToPredictionSignalList(List<PredictionSignalEntity> entities) {
        return entities.stream()
            .map(this::convertToPredictionSignal)
            .collect(Collectors.toList());
    }
    
    /**
     * Convert PredictionSignalEntity.SignalType to PredictionSignal.SignalType
     */
    private PredictionSignal.SignalType convertToModelSignalType(PredictionSignalEntity.SignalType signalType) {
        switch (signalType) {
            case LONG:
                return PredictionSignal.SignalType.LONG;
            case SHORT:
                return PredictionSignal.SignalType.SHORT;
            case REVERSAL:
                return PredictionSignal.SignalType.REVERSAL;
            default:
                return PredictionSignal.SignalType.HOLD;
        }
    }
    
    /**
     * Delete all signals for a symbol
     */
    public void deleteBySymbol(String symbol) {
        List<PredictionSignalEntity> entities = getBySymbol(symbol);
        predictionSignalRepository.deleteAll(entities);
    }
}
