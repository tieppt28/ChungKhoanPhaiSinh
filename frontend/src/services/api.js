import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Stock Data API
export const stockAPI = {
  // Get all available symbols
  getSymbols: () => api.get('/stocks/symbols'),
  
  // Get stock data for a symbol
  getStockData: (symbol) => api.get(`/stocks/${symbol}`),
  
  // Get stock data in TradingView format
  getStockDataForTradingView: (symbol) => api.get(`/stocks/${symbol}/tradingview`),
  
  // Get latest stock data for a symbol
  getLatestStockData: (symbol) => api.get(`/stocks/${symbol}/latest`),
  
  // Get stock data in date range
  getStockDataInRange: (symbol, startDate, endDate) => 
    api.get(`/stocks/${symbol}/range?startDate=${startDate}&endDate=${endDate}`),
  
  // Get stock data count
  getStockDataCount: (symbol) => api.get(`/stocks/${symbol}/count`),
  
  // Add new stock data
  addStockData: (symbol, stockData) => api.post(`/stocks/${symbol}`, stockData),
  
  // Add multiple stock data
  addMultipleStockData: (symbol, stockDataList) => 
    api.post(`/stocks/${symbol}/batch`, stockDataList),
  
  // Delete stock data
  deleteStockData: (symbol) => api.delete(`/stocks/${symbol}`),
  
  // Health check
  healthCheck: () => api.get('/stocks/health'),
};

// Prediction Signal API
export const signalAPI = {
  // Get all signals for a symbol
  getSignals: (symbol) => api.get(`/signals/${symbol}`),
  
  // Get signals in TradingView format
  getSignalsForTradingView: (symbol) => api.get(`/signals/${symbol}/tradingview`),
  
  // Get latest signal for a symbol
  getLatestSignal: (symbol) => api.get(`/signals/${symbol}/latest`),
  
  // Get signals in date range
  getSignalsInRange: (symbol, startDate, endDate) => 
    api.get(`/signals/${symbol}/range?startDate=${startDate}&endDate=${endDate}`),
  
  // Get signals by type
  getSignalsByType: (signalType) => api.get(`/signals/type/${signalType}`),
  
  // Get signals by symbol and type
  getSignalsBySymbolAndType: (symbol, signalType) => 
    api.get(`/signals/${symbol}/type/${signalType}`),
  
  // Get high confidence signals
  getHighConfidenceSignals: (threshold = 70.0) => 
    api.get(`/signals/high-confidence?threshold=${threshold}`),
  
  // Get recent signals
  getRecentSignals: (days = 7) => api.get(`/signals/recent?days=${days}`),
  
  // Get signal statistics
  getSignalStats: (symbol) => api.get(`/signals/${symbol}/stats`),
  
  // Add new signal
  addSignal: (symbol, signal) => api.post(`/signals/${symbol}`, signal),
  
  // Add multiple signals
  addMultipleSignals: (symbol, signals) => 
    api.post(`/signals/${symbol}/batch`, signals),
  
  // Delete signals
  deleteSignals: (symbol) => api.delete(`/signals/${symbol}`),
  
  // Health check
  healthCheck: () => api.get('/signals/health'),
};

// TradingView API
export const tradingViewAPI = {
  // Get available symbols
  getAvailableSymbols: () => api.get('/tradingview/symbols'),
  
  // Get real-time data
  getRealTimeData: (symbol) => api.get(`/tradingview/data/${symbol}`),
  
  // Get market overview
  getMarketOverview: () => api.get('/tradingview/market-overview'),
  
  // Get widget configuration
  getWidgetConfig: (symbol) => api.get(`/tradingview/widget-config/${symbol}`),
  
  // Health check
  healthCheck: () => api.get('/tradingview/health'),
};

// Market Analysis API
export const marketAnalysisAPI = {
  // Get comprehensive market analysis
  getMarketAnalysis: (symbol) => api.get(`/market-analysis/${symbol}`),
  
  // Get technical analysis only
  getTechnicalAnalysis: (symbol) => api.get(`/market-analysis/${symbol}/technical`),
  
  // Get prediction signal only
  getPredictionSignal: (symbol) => api.get(`/market-analysis/${symbol}/signal`),
  
  // Get market sentiment only
  getMarketSentiment: (symbol) => api.get(`/market-analysis/${symbol}/sentiment`),
  
  // Health check
  healthCheck: () => api.get('/market-analysis/health'),
};

// Utility functions
export const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('vi-VN');
};

export const formatDateTime = (dateString) => {
  return new Date(dateString).toLocaleString('vi-VN');
};

export const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    minimumFractionDigits: 2,
  }).format(price);
};

export const formatNumber = (number) => {
  return new Intl.NumberFormat('vi-VN').format(number);
};

export const formatPercentage = (percentage) => {
  return `${percentage.toFixed(2)}%`;
};

export default api;
