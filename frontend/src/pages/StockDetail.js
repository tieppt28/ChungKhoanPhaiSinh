import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import styled from 'styled-components';
import { ArrowLeft, TrendingUp, TrendingDown, AlertTriangle, Activity } from 'lucide-react';
import AdvancedTradingViewWithBackend from '../components/AdvancedTradingViewWithBackend';
import { stockAPI, signalAPI, formatPrice, formatDateTime, formatPercentage } from '../services/api';

const StockDetailContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
`;

const BackButton = styled(Link)`
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #2563eb;
  text-decoration: none;
  font-weight: 500;
  margin-bottom: 20px;
  
  &:hover {
    color: #1d4ed8;
  }
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
`;

const StockInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const StockSymbol = styled.h1`
  color: #ffffff;
  font-size: 36px;
  font-weight: bold;
  margin: 0;
`;

const StockPrice = styled.div`
  color: #10b981;
  font-size: 24px;
  font-weight: bold;
`;

const StockChange = styled.div`
  color: ${props => props.positive ? '#10b981' : '#ef4444'};
  font-size: 16px;
`;

const SignalBadge = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 500;
  font-size: 14px;
  background-color: ${props => {
    switch (props.type) {
      case 'LONG': return '#10b981';
      case 'SHORT': return '#ef4444';
      case 'REVERSAL': return '#f59e0b';
      default: return '#6b7280';
    }
  }};
  color: #ffffff;
`;

const ContentGrid = styled.div`
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
  margin-bottom: 30px;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const ChartSection = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
`;

const SectionTitle = styled.h2`
  color: #ffffff;
  font-size: 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  
  svg {
    color: #2563eb;
  }
`;

const InfoSection = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
`;

const InfoGrid = styled.div`
  display: grid;
  gap: 15px;
`;

const InfoItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #333;
  
  &:last-child {
    border-bottom: none;
  }
`;

const InfoLabel = styled.span`
  color: #666;
  font-size: 14px;
`;

const InfoValue = styled.span`
  color: #ffffff;
  font-weight: 500;
`;

const SignalsSection = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
`;

const SignalItem = styled.div`
  background-color: #1f2937;
  border: 1px solid #333;
  border-radius: 6px;
  padding: 15px;
  margin-bottom: 15px;
  
  &:last-child {
    margin-bottom: 0;
  }
`;

const SignalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
`;

const SignalType = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  
  svg {
    width: 16px;
    height: 16px;
  }
`;

const SignalConfidence = styled.div`
  color: #10b981;
  font-weight: bold;
`;

const SignalReason = styled.div`
  color: #666;
  font-size: 14px;
  margin-bottom: 5px;
`;

const SignalPrice = styled.div`
  color: #ffffff;
  font-weight: 500;
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

function StockDetail() {
  const { symbol } = useParams();
  const [stockData, setStockData] = useState(null);
  const [signals, setSignals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (symbol) {
      loadStockData();
    }
  }, [symbol]);

  const loadStockData = async () => {
    try {
      setLoading(true);
      setError(null);

      const [stockResponse, signalsResponse] = await Promise.all([
        stockAPI.getLatestStockData(symbol),
        signalAPI.getSignals(symbol)
      ]);

      setStockData(stockResponse.data);
      setSignals(signalsResponse.data || []);
      setLoading(false);

    } catch (err) {
      console.error('Error loading stock data:', err);
      setError('Không thể tải dữ liệu cho mã cổ phiếu này.');
      setLoading(false);
    }
  };

  const getSignalIcon = (signalType) => {
    switch (signalType) {
      case 'LONG':
        return <TrendingUp size={16} />;
      case 'SHORT':
        return <TrendingDown size={16} />;
      case 'REVERSAL':
        return <AlertTriangle size={16} />;
      default:
        return <Activity size={16} />;
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

  if (loading) {
    return (
      <StockDetailContainer>
        <LoadingMessage>Đang tải dữ liệu...</LoadingMessage>
      </StockDetailContainer>
    );
  }

  if (error) {
    return (
      <StockDetailContainer>
        <BackButton to="/">
          <ArrowLeft size={16} />
          Quay lại Dashboard
        </BackButton>
        <ErrorMessage>{error}</ErrorMessage>
      </StockDetailContainer>
    );
  }

  const latestSignal = signals.length > 0 ? signals[0] : null;

  return (
    <StockDetailContainer>
      <BackButton to="/">
        <ArrowLeft size={16} />
        Quay lại Dashboard
      </BackButton>

      <Header>
        <StockInfo>
          <StockSymbol>{symbol}</StockSymbol>
          {stockData && (
            <>
              <StockPrice>{formatPrice(stockData.closePrice)}</StockPrice>
              <StockChange positive={true}>
                +2.5% (+1,250 VND)
              </StockChange>
            </>
          )}
        </StockInfo>
        
        {latestSignal && (
          <SignalBadge type={latestSignal.signalType}>
            {getSignalIcon(latestSignal.signalType)}
            {latestSignal.signalType}
            ({latestSignal.confidence?.toFixed(1)}%)
          </SignalBadge>
        )}
      </Header>

      <ContentGrid>
        <ChartSection>
          <SectionTitle>
            <Activity />
            Biểu đồ Giá
          </SectionTitle>
          <AdvancedTradingViewWithBackend />
        </ChartSection>

        <InfoSection>
          <SectionTitle>
            <Activity />
            Thông tin Cổ phiếu
          </SectionTitle>
          {stockData && (
            <InfoGrid>
              <InfoItem>
                <InfoLabel>Giá mở cửa</InfoLabel>
                <InfoValue>{formatPrice(stockData.openPrice)}</InfoValue>
              </InfoItem>
              <InfoItem>
                <InfoLabel>Giá cao nhất</InfoLabel>
                <InfoValue>{formatPrice(stockData.highPrice)}</InfoValue>
              </InfoItem>
              <InfoItem>
                <InfoLabel>Giá thấp nhất</InfoLabel>
                <InfoValue>{formatPrice(stockData.lowPrice)}</InfoValue>
              </InfoItem>
              <InfoItem>
                <InfoLabel>Giá đóng cửa</InfoLabel>
                <InfoValue>{formatPrice(stockData.closePrice)}</InfoValue>
              </InfoItem>
              <InfoItem>
                <InfoLabel>Khối lượng</InfoLabel>
                <InfoValue>{stockData.volume?.toLocaleString()}</InfoValue>
              </InfoItem>
              <InfoItem>
                <InfoLabel>Thời gian</InfoLabel>
                <InfoValue>{formatDateTime(stockData.timestamp)}</InfoValue>
              </InfoItem>
            </InfoGrid>
          )}
        </InfoSection>
      </ContentGrid>

      <SignalsSection>
        <SectionTitle>
          <Activity />
          Tín hiệu Dự đoán ({signals.length})
        </SectionTitle>
        
        {signals.length === 0 ? (
          <div style={{ color: '#666', textAlign: 'center', padding: '20px' }}>
            Chưa có tín hiệu dự đoán nào
          </div>
        ) : (
          signals.map((signal, index) => (
            <SignalItem key={signal.id || index}>
              <SignalHeader>
                <SignalType style={{ color: getSignalColor(signal.signalType) }}>
                  {getSignalIcon(signal.signalType)}
                  {signal.signalType}
                </SignalType>
                <SignalConfidence>
                  {signal.confidence?.toFixed(1)}%
                </SignalConfidence>
              </SignalHeader>
              <SignalReason>{signal.reason}</SignalReason>
              <SignalPrice>
                Giá: {formatPrice(signal.price)} - {formatDateTime(signal.timestamp)}
              </SignalPrice>
            </SignalItem>
          ))
        )}
      </SignalsSection>
    </StockDetailContainer>
  );
}

export default StockDetail;

