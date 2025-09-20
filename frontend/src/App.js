import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import styled from 'styled-components';
import Header from './components/Header';
import DashboardBasic from './pages/DashboardBasic';
import StockDetail from './pages/StockDetail';
import Signals from './pages/Signals';
import './App.css';

const AppContainer = styled.div`
  min-height: 100vh;
  background-color: #0a0a0a;
  color: #ffffff;
`;

const MainContent = styled.main`
  padding-top: 80px; /* Account for fixed header */
`;

function App() {
  return (
    <AppContainer>
      <Router>
        <Header />
        <MainContent>
          <Routes>
            <Route path="/" element={<DashboardBasic />} />
            <Route path="/stock/:symbol" element={<StockDetail />} />
            <Route path="/signals" element={<Signals />} />
          </Routes>
        </MainContent>
      </Router>
    </AppContainer>
  );
}

export default App;
