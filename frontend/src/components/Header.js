import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { TrendingUp, BarChart3, Signal, Globe } from 'lucide-react';

const HeaderContainer = styled.header`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #1a1a1a;
  border-bottom: 1px solid #333;
  z-index: 1000;
  padding: 0 20px;
`;

const HeaderContent = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
`;

const Logo = styled(Link)`
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #ffffff;
  font-size: 24px;
  font-weight: bold;
  
  svg {
    margin-right: 10px;
    color: #2563eb;
  }
  
  &:hover {
    color: #2563eb;
  }
`;

const Nav = styled.nav`
  display: flex;
  gap: 30px;
`;

const NavLink = styled(Link)`
  display: flex;
  align-items: center;
  text-decoration: none;
  color: ${props => props.active ? '#2563eb' : '#ffffff'};
  font-weight: 500;
  padding: 10px 15px;
  border-radius: 6px;
  transition: all 0.2s ease;
  
  svg {
    margin-right: 8px;
    width: 18px;
    height: 18px;
  }
  
  &:hover {
    color: #2563eb;
    background-color: #1f2937;
  }
`;

const StatusIndicator = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #10b981;
  
  &::before {
    content: '';
    width: 8px;
    height: 8px;
    background-color: #10b981;
    border-radius: 50%;
    animation: pulse 2s infinite;
  }
  
  @keyframes pulse {
    0% { opacity: 1; }
    50% { opacity: 0.5; }
    100% { opacity: 1; }
  }
`;

function Header() {
  const location = useLocation();
  
  return (
    <HeaderContainer>
      <HeaderContent>
        <Logo to="/">
          <TrendingUp size={28} />
          Stock Prediction
        </Logo>
        
        <Nav>
          <NavLink 
            to="/" 
            active={location.pathname === '/' ? 1 : 0}
          >
            <BarChart3 size={18} />
            TradingView
          </NavLink>
          
          <NavLink 
            to="/signals" 
            active={location.pathname === '/signals' ? 1 : 0}
          >
            <Signal size={18} />
            Signals
          </NavLink>
          
        </Nav>
        
        <StatusIndicator>
          Live Data
        </StatusIndicator>
      </HeaderContent>
    </HeaderContainer>
  );
}

export default Header;
