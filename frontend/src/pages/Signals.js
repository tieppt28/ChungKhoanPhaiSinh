import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TrendingUp, TrendingDown, AlertTriangle, Activity, Filter } from 'lucide-react';
import { signalAPI, formatPrice, formatDateTime } from '../services/api';

const SignalsContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
`;

const PageTitle = styled.h1`
  color: #ffffff;
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
`;

const FiltersSection = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
`;

const FilterRow = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
`;

const FilterLabel = styled.label`
  color: #ffffff;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  
  svg {
    color: #2563eb;
  }
`;

const FilterSelect = styled.select`
  background-color: #333;
  color: #ffffff;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  
  option {
    background-color: #333;
    color: #ffffff;
  }
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
`;

const StatCard = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
`;

const StatIcon = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
  
  svg {
    width: 32px;
    height: 32px;
    color: ${props => props.color || '#2563eb'};
  }
`;

const StatValue = styled.div`
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 5px;
`;

const StatLabel = styled.div`
  color: #666;
  font-size: 14px;
`;

const SignalsList = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
`;

const SignalItem = styled.div`
  background-color: #1f2937;
  border: 1px solid #333;
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 15px;
  
  &:last-child {
    margin-bottom: 0;
  }
`;

const SignalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
`;

const SignalInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 15px;
`;

const SignalType = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  font-size: 16px;
  
  svg {
    width: 18px;
    height: 18px;
  }
`;

const SignalConfidence = styled.div`
  color: #10b981;
  font-weight: bold;
  font-size: 16px;
`;

const SignalSymbol = styled.div`
  color: #2563eb;
  font-weight: bold;
  font-size: 18px;
`;

const SignalDetails = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 15px;
`;

const DetailItem = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
`;

const DetailLabel = styled.span`
  color: #666;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
`;

const DetailValue = styled.span`
  color: #ffffff;
  font-weight: 500;
`;

const SignalReason = styled.div`
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  background-color: #111827;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid ${props => props.color || '#2563eb'};
`;

const LoadingMessage = styled.div`
  text-align: center;
  color: #666;
  font-size: 16px;
  padding: 40px;
`;

const ErrorMessage = styled.div`
  background-color: #1f2937;
  border: 1px solid #ef4444;
  border-radius: 6px;
  padding: 15px;
  color: #ef4444;
  margin: 20px 0;
`;

const EmptyMessage = styled.div`
  text-align: center;
  color: #666;
  font-size: 16px;
  padding: 40px;
`;

function Signals() {
  const [signals, setSignals] = useState([]);
  const [filteredSignals, setFilteredSignals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    signalType: 'ALL',
    confidence: 'ALL',
    symbol: 'ALL'
  });

  useEffect(() => {
    loadSignals();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [signals, filters]);

  const loadSignals = async () => {
    try {
      setLoading(true);
      setError(null);

      // Load recent signals (last 30 days)
      const response = await signalAPI.getRecentSignals(30);
      setSignals(response.data || []);
      setLoading(false);

    } catch (err) {
      console.error('Error loading signals:', err);
      setError('Không thể tải dữ liệu tín hiệu.');
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...signals];

    if (filters.signalType !== 'ALL') {
      filtered = filtered.filter(signal => signal.signalType === filters.signalType);
    }

    if (filters.confidence !== 'ALL') {
      const threshold = parseFloat(filters.confidence);
      filtered = filtered.filter(signal => signal.confidence >= threshold);
    }

    if (filters.symbol !== 'ALL') {
      filtered = filtered.filter(signal => signal.symbol === filters.symbol);
    }

    setFilteredSignals(filtered);
  };

  const getSignalIcon = (signalType) => {
    switch (signalType) {
      case 'LONG':
        return <TrendingUp size={18} />;
      case 'SHORT':
        return <TrendingDown size={18} />;
      case 'REVERSAL':
        return <AlertTriangle size={18} />;
      default:
        return <Activity size={18} />;
    }
  };

  const getSignalColor = (signalType) => {
    switch (signalType) {
      case 'LONG':
        return '#10b981';
      case 'SHORT':
        return '#ef4444';
      case 'REVERSAL':
        return '#f59e0b';
      default:
        return '#6b7280';
    }
  };

  const getSignalStats = () => {
    const stats = { LONG: 0, SHORT: 0, REVERSAL: 0, HOLD: 0 };
    signals.forEach(signal => {
      if (signal.signalType) {
        stats[signal.signalType] = (stats[signal.signalType] || 0) + 1;
      }
    });
    return stats;
  };

  const getUniqueSymbols = () => {
    const symbols = [...new Set(signals.map(signal => signal.symbol))];
    return symbols.sort();
  };

  const getHighConfidenceCount = () => {
    return signals.filter(signal => signal.confidence >= 70).length;
  };

  if (loading) {
    return (
      <SignalsContainer>
        <LoadingMessage>Đang tải tín hiệu dự đoán...</LoadingMessage>
      </SignalsContainer>
    );
  }

  if (error) {
    return (
      <SignalsContainer>
        <ErrorMessage>{error}</ErrorMessage>
      </SignalsContainer>
    );
  }

  const signalStats = getSignalStats();
  const uniqueSymbols = getUniqueSymbols();

  return (
    <SignalsContainer>
      <PageTitle>Tín hiệu Dự đoán</PageTitle>

      <StatsGrid>
        <StatCard>
          <StatIcon color="#10b981">
            <TrendingUp />
          </StatIcon>
          <StatValue>{signalStats.LONG}</StatValue>
          <StatLabel>Tín hiệu MUA</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#ef4444">
            <TrendingDown />
          </StatIcon>
          <StatValue>{signalStats.SHORT}</StatValue>
          <StatLabel>Tín hiệu BÁN</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#f59e0b">
            <AlertTriangle />
          </StatIcon>
          <StatValue>{signalStats.REVERSAL}</StatValue>
          <StatLabel>Tín hiệu ĐẢO CHIỀU</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#2563eb">
            <Activity />
          </StatIcon>
          <StatValue>{getHighConfidenceCount()}</StatValue>
          <StatLabel>Độ tin cậy cao (≥70%)</StatLabel>
        </StatCard>
      </StatsGrid>

      <FiltersSection>
        <FilterRow>
          <FilterLabel>
            <Filter size={16} />
            Lọc theo:
          </FilterLabel>
          
          <FilterSelect
            value={filters.signalType}
            onChange={(e) => setFilters({...filters, signalType: e.target.value})}
          >
            <option value="ALL">Tất cả tín hiệu</option>
            <option value="LONG">MUA (LONG)</option>
            <option value="SHORT">BÁN (SHORT)</option>
            <option value="REVERSAL">ĐẢO CHIỀU</option>
            <option value="HOLD">GIỮ (HOLD)</option>
          </FilterSelect>

          <FilterSelect
            value={filters.confidence}
            onChange={(e) => setFilters({...filters, confidence: e.target.value})}
          >
            <option value="ALL">Tất cả độ tin cậy</option>
            <option value="90">≥ 90%</option>
            <option value="80">≥ 80%</option>
            <option value="70">≥ 70%</option>
            <option value="60">≥ 60%</option>
          </FilterSelect>

          <FilterSelect
            value={filters.symbol}
            onChange={(e) => setFilters({...filters, symbol: e.target.value})}
          >
            <option value="ALL">Tất cả mã cổ phiếu</option>
            {uniqueSymbols.map(symbol => (
              <option key={symbol} value={symbol}>{symbol}</option>
            ))}
          </FilterSelect>
        </FilterRow>
      </FiltersSection>

      <SignalsList>
        <h2 style={{ color: '#ffffff', marginBottom: '20px' }}>
          Danh sách Tín hiệu ({filteredSignals.length})
        </h2>

        {filteredSignals.length === 0 ? (
          <EmptyMessage>
            Không có tín hiệu nào phù hợp với bộ lọc
          </EmptyMessage>
        ) : (
          filteredSignals.map((signal, index) => (
            <SignalItem key={signal.id || index}>
              <SignalHeader>
                <SignalInfo>
                  <SignalSymbol>{signal.symbol}</SignalSymbol>
                  <SignalType style={{ color: getSignalColor(signal.signalType) }}>
                    {getSignalIcon(signal.signalType)}
                    {signal.signalType}
                  </SignalType>
                </SignalInfo>
                <SignalConfidence>
                  {signal.confidence?.toFixed(1)}%
                </SignalConfidence>
              </SignalHeader>

              <SignalDetails>
                <DetailItem>
                  <DetailLabel>Giá</DetailLabel>
                  <DetailValue>{formatPrice(signal.price)}</DetailValue>
                </DetailItem>
                <DetailItem>
                  <DetailLabel>Thời gian</DetailLabel>
                  <DetailValue>{formatDateTime(signal.timestamp)}</DetailValue>
                </DetailItem>
                <DetailItem>
                  <DetailLabel>Độ tin cậy</DetailLabel>
                  <DetailValue>{signal.confidence?.toFixed(1)}%</DetailValue>
                </DetailItem>
              </SignalDetails>

              <SignalReason color={getSignalColor(signal.signalType)}>
                {signal.reason}
              </SignalReason>
            </SignalItem>
          ))
        )}
      </SignalsList>
    </SignalsContainer>
  );
}

export default Signals;

