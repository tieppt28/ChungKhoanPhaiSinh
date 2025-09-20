import React, { useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { marketAnalysisAPI } from '../services/api';

const Container = styled.div`
  width: 100%;
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
`;

const ChartContainer = styled.div`
  flex: 1;
  height: 700px;
  background-color: #1a1a1a;
  border-radius: 8px;
  border: 1px solid #333;
  position: relative;
  overflow: hidden;
`;

const AnalysisContainer = styled.div`
  width: 400px;
  background-color: #1a1a1a;
  border-radius: 8px;
  border: 1px solid #333;
  padding: 20px;
  overflow-y: auto;
  height: 700px;
`;

const SymbolSelector = styled.select`
  background-color: #333;
  color: #ffffff;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  width: 100%;
  margin-bottom: 20px;
  
  option {
    background-color: #333;
    color: #ffffff;
  }
`;


const AnalysisPanel = styled.div`
  width: 100%;
`;

const AnalysisTitle = styled.h4`
  color: #ffffff;
  margin: 0 0 15px 0;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 10px;
`;

const SignalBadge = styled.div`
  display: inline-block;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  background-color: ${props => {
    switch(props.type) {
      case 'LONG': return '#10b981';
      case 'SHORT': return '#ef4444';
      case 'HOLD': return '#6b7280';
      default: return '#f59e0b';
    }
  }};
  color: #ffffff;
  margin-left: 10px;
`;

const AnalysisSection = styled.div`
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #333;
  
  &:last-child {
    border-bottom: none;
  }
`;

const SectionTitle = styled.h5`
  color: #ffffff;
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 600;
`;

const AnalysisItem = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 13px;
`;

const AnalysisLabel = styled.span`
  color: #666;
`;

const AnalysisValue = styled.span`
  color: ${props => props.color || '#ffffff'};
  font-weight: 500;
`;

const ConfidenceBar = styled.div`
  width: 100%;
  height: 6px;
  background-color: #333;
  border-radius: 3px;
  overflow: hidden;
  margin-top: 5px;
`;

const ConfidenceFill = styled.div`
  height: 100%;
  background-color: ${props => {
    if (props.confidence >= 80) return '#10b981';
    if (props.confidence >= 60) return '#f59e0b';
    return '#ef4444';
  }};
  width: ${props => props.confidence}%;
  transition: width 0.3s ease;
`;

const LoadingMessage = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #666;
  font-size: 16px;
  text-align: center;
  z-index: 10;
`;

const ErrorMessage = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #ef4444;
  font-size: 16px;
  text-align: center;
  z-index: 10;
`;


// Global symbols
const SYMBOLS = [
  'AAPL', 'GOOGL', 'MSFT', 'TSLA', 'AMZN', 'NVDA', 'META', 'NFLX', 
  'BABA', 'BTCUSD', 'ETHUSD', 'SPY', 'QQQ', 'VTI', 'ARKK', 'GME'
];

function AdvancedTradingViewWithBackend() {
  const containerRef = useRef();
  const [selectedSymbol, setSelectedSymbol] = useState('AAPL');
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadTradingViewWidget();
    loadMarketAnalysis();
  }, [selectedSymbol]);

  const loadTradingViewWidget = () => {
    if (containerRef.current) {
      containerRef.current.innerHTML = '';
    }

    const script = document.createElement('script');
    script.src = 'https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js';
    script.async = true;
    script.innerHTML = JSON.stringify({
      "autosize": true,
      "symbol": `NASDAQ:${selectedSymbol}`,
      "interval": "1D",
      "timezone": "Etc/UTC",
      "theme": "dark",
      "style": "1",
      "locale": "en",
      "toolbar_bg": "#1a1a1a",
      "enable_publishing": false,
      "hide_top_toolbar": false,
      "hide_legend": false,
      "save_image": false,
      "container_id": `tradingview_${selectedSymbol}`,
      "studies": [
        "RSI@tv-basicstudies",
        "MACD@tv-basicstudies",
        "EMA@tv-basicstudies",
        "Volume@tv-basicstudies"
      ],
      "overrides": {
        "paneProperties.background": "#1a1a1a",
        "paneProperties.vertGridProperties.color": "#333",
        "paneProperties.horzGridProperties.color": "#333",
        "symbolWatermarkProperties.transparency": 90,
        "scalesProperties.textColor": "#ffffff",
        "scalesProperties.lineColor": "#333"
      }
    });

    if (containerRef.current) {
      containerRef.current.appendChild(script);
    }
  };

  const loadMarketAnalysis = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await marketAnalysisAPI.getMarketAnalysis(selectedSymbol);
      setAnalysis(response.data);
      
    } catch (err) {
      console.error('Error loading market analysis:', err);
      setError('Không thể tải phân tích thị trường');
    } finally {
      setLoading(false);
    }
  };

  const getSignalColor = (signalType) => {
    switch(signalType) {
      case 'LONG': return '#10b981';
      case 'SHORT': return '#ef4444';
      case 'HOLD': return '#6b7280';
      default: return '#f59e0b';
    }
  };

  const getRSIColor = (rsi) => {
    if (rsi > 70) return '#ef4444';
    if (rsi < 30) return '#10b981';
    return '#ffffff';
  };

  const getMACDColor = (macd) => {
    return macd > 0 ? '#10b981' : '#ef4444';
  };

  return (
    <Container>
      <ChartContainer>
        <div
          ref={containerRef}
          id={`tradingview_${selectedSymbol}`}
          style={{ width: '100%', height: '700px' }}
        />

        {loading && (
          <LoadingMessage>
            Đang tải biểu đồ TradingView...
          </LoadingMessage>
        )}
        {error && (
          <ErrorMessage>
            {error}
          </ErrorMessage>
        )}
      </ChartContainer>

      <AnalysisContainer>
        <SymbolSelector 
          value={selectedSymbol} 
          onChange={(e) => setSelectedSymbol(e.target.value)}
        >
          {SYMBOLS.map(symbol => (
            <option key={symbol} value={symbol}>{symbol}</option>
          ))}
        </SymbolSelector>

        {analysis && (
          <AnalysisPanel>
          <AnalysisTitle>
            📊 Phân tích Thị trường
            {analysis.predictionSignal && (
              <SignalBadge type={analysis.predictionSignal.signalType}>
                {analysis.predictionSignal.signalType}
              </SignalBadge>
            )}
          </AnalysisTitle>
          
          {/* Technical Analysis Section */}
          {analysis.technicalAnalysis && (
            <AnalysisSection>
              <SectionTitle>🔍 Phân tích Kỹ thuật</SectionTitle>
              
              <AnalysisItem>
                <AnalysisLabel>RSI (14):</AnalysisLabel>
                <AnalysisValue color={getRSIColor(analysis.technicalAnalysis.rsi)}>
                  {analysis.technicalAnalysis.rsi?.toFixed(2) || 'N/A'}
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>MACD:</AnalysisLabel>
                <AnalysisValue color={getMACDColor(analysis.technicalAnalysis.macd)}>
                  {analysis.technicalAnalysis.macd?.toFixed(4) || 'N/A'}
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>EMA 20:</AnalysisLabel>
                <AnalysisValue>{analysis.technicalAnalysis.ema20?.toFixed(2) || 'N/A'}</AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>EMA 50:</AnalysisLabel>
                <AnalysisValue>{analysis.technicalAnalysis.ema50?.toFixed(2) || 'N/A'}</AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>BB Upper:</AnalysisLabel>
                <AnalysisValue>{analysis.technicalAnalysis.bbUpper?.toFixed(2) || 'N/A'}</AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>BB Lower:</AnalysisLabel>
                <AnalysisValue>{analysis.technicalAnalysis.bbLower?.toFixed(2) || 'N/A'}</AnalysisValue>
              </AnalysisItem>
            </AnalysisSection>
          )}

          {/* Prediction Signal Section */}
          {analysis.predictionSignal && (
            <AnalysisSection>
              <SectionTitle>🎯 Tín hiệu Dự đoán</SectionTitle>
              
              <AnalysisItem>
                <AnalysisLabel>Tín hiệu:</AnalysisLabel>
                <AnalysisValue color={getSignalColor(analysis.predictionSignal.signalType)}>
                  {analysis.predictionSignal.signalType}
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>Độ tin cậy:</AnalysisLabel>
                <AnalysisValue color="#f59e0b">
                  {analysis.predictionSignal.confidence?.toFixed(1) || 'N/A'}%
                </AnalysisValue>
              </AnalysisItem>
              
              <ConfidenceBar>
                <ConfidenceFill confidence={analysis.predictionSignal.confidence || 0} />
              </ConfidenceBar>
              
              <AnalysisItem>
                <AnalysisLabel>Lý do:</AnalysisLabel>
                <AnalysisValue style={{ fontSize: '12px', color: '#888' }}>
                  {analysis.predictionSignal.reasoning || 'N/A'}
                </AnalysisValue>
              </AnalysisItem>
              
              {analysis.predictionSignal.targetPrice && (
                <AnalysisItem>
                  <AnalysisLabel>Giá mục tiêu:</AnalysisLabel>
                  <AnalysisValue color="#10b981">
                    ${analysis.predictionSignal.targetPrice.toFixed(2)}
                  </AnalysisValue>
                </AnalysisItem>
              )}
              
              {analysis.predictionSignal.stopLoss && (
                <AnalysisItem>
                  <AnalysisLabel>Stop Loss:</AnalysisLabel>
                  <AnalysisValue color="#ef4444">
                    ${analysis.predictionSignal.stopLoss.toFixed(2)}
                  </AnalysisValue>
                </AnalysisItem>
              )}
            </AnalysisSection>
          )}

          {/* Market Sentiment Section */}
          {analysis.marketSentiment && (
            <AnalysisSection>
              <SectionTitle>📈 Tâm lý Thị trường</SectionTitle>
              
              <AnalysisItem>
                <AnalysisLabel>Tâm lý tổng thể:</AnalysisLabel>
                <AnalysisValue color={analysis.marketSentiment.overallSentiment?.includes('BULLISH') ? '#10b981' : '#ef4444'}>
                  {analysis.marketSentiment.overallSentiment || 'N/A'}
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>Xu hướng ngắn hạn:</AnalysisLabel>
                <AnalysisValue color={analysis.marketSentiment.shortTermTrend > 0 ? '#10b981' : '#ef4444'}>
                  {analysis.marketSentiment.shortTermTrend?.toFixed(2) || 'N/A'}%
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>Xu hướng trung hạn:</AnalysisLabel>
                <AnalysisValue color={analysis.marketSentiment.mediumTermTrend > 0 ? '#10b981' : '#ef4444'}>
                  {analysis.marketSentiment.mediumTermTrend?.toFixed(2) || 'N/A'}%
                </AnalysisValue>
              </AnalysisItem>
              
              <AnalysisItem>
                <AnalysisLabel>Độ biến động:</AnalysisLabel>
                <AnalysisValue>
                  {(analysis.marketSentiment.volatility * 100)?.toFixed(2) || 'N/A'}%
                </AnalysisValue>
              </AnalysisItem>
            </AnalysisSection>
          )}

          {/* Data Quality */}
          <AnalysisSection>
            <SectionTitle>ℹ️ Thông tin</SectionTitle>
            <AnalysisItem>
              <AnalysisLabel>Nguồn dữ liệu:</AnalysisLabel>
              <AnalysisValue color={analysis.dataQuality === 'real' ? '#10b981' : '#f59e0b'}>
                {analysis.dataQuality === 'real' ? 'Thực tế' : 'Mô phỏng'}
              </AnalysisValue>
            </AnalysisItem>
            <AnalysisItem>
              <AnalysisLabel>Cập nhật:</AnalysisLabel>
              <AnalysisValue style={{ fontSize: '12px' }}>
                {new Date(analysis.timestamp).toLocaleString('vi-VN')}
              </AnalysisValue>
            </AnalysisItem>
          </AnalysisSection>
        </AnalysisPanel>
        )}
      </AnalysisContainer>
    </Container>
  );
}

export default AdvancedTradingViewWithBackend;
