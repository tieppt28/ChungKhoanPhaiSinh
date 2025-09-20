import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TrendingUp, TrendingDown, BarChart3, Signal } from 'lucide-react';
import BasicTradingView from '../components/BasicTradingView';

const DashboardContainer = styled.div`
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

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
    width: 40px;
    height: 40px;
    color: ${props => props.color || '#2563eb'};
  }
`;

const StatValue = styled.div`
  color: #ffffff;
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
`;

const StatLabel = styled.div`
  color: #666;
  font-size: 14px;
`;

const ChartSection = styled.div`
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
`;

const SectionTitle = styled.h2`
  color: #ffffff;
  font-size: 24px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  
  svg {
    color: #2563eb;
  }
`;

const SymbolSelector = styled.select`
  background-color: #333;
  color: #ffffff;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 10px 15px;
  font-size: 16px;
  margin-bottom: 20px;
  width: 200px;
  
  option {
    background-color: #333;
    color: #ffffff;
  }
`;

function DashboardSimple() {
  const [selectedSymbol, setSelectedSymbol] = useState('AAPL');
  const [symbols] = useState(['AAPL', 'GOOGL', 'MSFT', 'TSLA', 'AMZN', 'NVDA', 'META', 'NFLX', 'BABA', 'BTCUSD']);

  return (
    <DashboardContainer>
      <PageTitle>Dashboard - Hệ thống Dự đoán Chứng khoán</PageTitle>

      <StatsGrid>
        <StatCard>
          <StatIcon color="#2563eb">
            <BarChart3 />
          </StatIcon>
          <StatValue>{symbols.length}</StatValue>
          <StatLabel>Tổng số mã cổ phiếu</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#10b981">
            <TrendingUp />
          </StatIcon>
          <StatValue>5</StatValue>
          <StatLabel>Tín hiệu MUA (LONG)</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#ef4444">
            <TrendingDown />
          </StatIcon>
          <StatValue>3</StatValue>
          <StatLabel>Tín hiệu BÁN (SHORT)</StatLabel>
        </StatCard>

        <StatCard>
          <StatIcon color="#f59e0b">
            <Signal />
          </StatIcon>
          <StatValue>8</StatValue>
          <StatLabel>Tín hiệu hoạt động</StatLabel>
        </StatCard>
      </StatsGrid>

      <ChartSection>
        <SectionTitle>
          <BarChart3 />
          Biểu đồ Giá cổ phiếu
        </SectionTitle>
        
        <SymbolSelector 
          value={selectedSymbol} 
          onChange={(e) => setSelectedSymbol(e.target.value)}
        >
          {symbols.map(symbol => (
            <option key={symbol} value={symbol}>{symbol}</option>
          ))}
        </SymbolSelector>

        {selectedSymbol && (
          <BasicTradingView />
        )}
      </ChartSection>
    </DashboardContainer>
  );
}

export default DashboardSimple;
