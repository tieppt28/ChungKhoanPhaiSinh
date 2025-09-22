import React, { useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { TrendingUp, TrendingDown, Activity, AlertTriangle } from 'lucide-react';

const Container = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
`;

const ChartContainer = styled.div`
  width: 100%;
  height: 400px;
  background-color: #0a0a0a;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 16px;
`;

const AnalysisContainer = styled.div`
  margin-top: 20px;
  padding: 20px;
  background-color: #0a0a0a;
  border-radius: 8px;
  border: 1px solid #333;
`;

const AnalysisGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
`;

const AnalysisItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #1a1a1a;
  border-radius: 4px;
  border: 1px solid #333;
`;

const Label = styled.span`
  color: #666;
  font-size: 14px;
`;

const Value = styled.span`
  color: #ffffff;
  font-weight: 600;
  font-size: 14px;
`;

const Recommendation = styled.div`
  padding: 15px;
  background-color: ${props => props.type === 'BUY' ? '#10b981' : props.type === 'SELL' ? '#ef4444' : '#f59e0b'};
  color: white;
  border-radius: 8px;
  text-align: center;
  font-weight: 600;
  margin-top: 15px;
`;

const SimpleTradingView = ({ symbol = 'AAPL' }) => {
  const [loading, setLoading] = useState(true);
  const [analysis, setAnalysis] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));

        const mockData = {
          symbol: symbol,
          currentPrice: 150.25,
          change: 2.35,
          changePercent: 1.59,
          volume: 1250000,
          rsi: 65.2,
          macd: 1.25,
          ema20: 148.50,
          ema50: 145.75,
          recommendation: 'BUY',
          confidence: 78.5,
          summary: 'Cổ phiếu đang trong xu hướng tăng mạnh với tín hiệu mua rõ ràng.'
        };

        setAnalysis(mockData);
      } catch (error) {
        console.error('Error loading data:', error);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [symbol]);

  if (loading) {
    return (
      <Container>
        <ChartContainer>
          <Activity className="animate-spin" size={24} />
          <span style={{ marginLeft: '10px' }}>Đang tải dữ liệu...</span>
        </ChartContainer>
      </Container>
    );
  }

  if (!analysis) {
    return (
      <Container>
        <ChartContainer>
          <AlertTriangle size={24} />
          <span style={{ marginLeft: '10px' }}>Không thể tải dữ liệu</span>
        </ChartContainer>
      </Container>
    );
  }

  return (
    <Container>
      <ChartContainer>
        <div style={{ textAlign: 'center' }}>
          <TrendingUp size={48} color="#10b981" />
          <div style={{ marginTop: '10px', fontSize: '18px', fontWeight: '600' }}>
            {analysis.symbol} - ${analysis.currentPrice}
          </div>
          <div style={{ color: analysis.change > 0 ? '#10b981' : '#ef4444' }}>
            {analysis.change > 0 ? '+' : ''}{analysis.change} ({analysis.changePercent}%)
          </div>
        </div>
      </ChartContainer>

      <AnalysisContainer>
        <h3 style={{ color: '#ffffff', marginBottom: '15px' }}>Phân Tích Kỹ Thuật</h3>
        
        <AnalysisGrid>
          <AnalysisItem>
            <Label>Giá hiện tại</Label>
            <Value>${analysis.currentPrice}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>Thay đổi</Label>
            <Value style={{ color: analysis.change > 0 ? '#10b981' : '#ef4444' }}>
              {analysis.change > 0 ? '+' : ''}{analysis.change}
            </Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>Volume</Label>
            <Value>{analysis.volume.toLocaleString()}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>RSI</Label>
            <Value>{analysis.rsi}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>MACD</Label>
            <Value>{analysis.macd}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>EMA 20</Label>
            <Value>${analysis.ema20}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>EMA 50</Label>
            <Value>${analysis.ema50}</Value>
          </AnalysisItem>
          
          <AnalysisItem>
            <Label>Độ tin cậy</Label>
            <Value>{analysis.confidence}%</Value>
          </AnalysisItem>
        </AnalysisGrid>

        <Recommendation type={analysis.recommendation}>
          {analysis.recommendation} - {analysis.summary}
        </Recommendation>
      </AnalysisContainer>
    </Container>
  );
};

export default SimpleTradingView;
