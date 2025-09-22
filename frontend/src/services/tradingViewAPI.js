// TradingView API Service
const TRADINGVIEW_API_BASE = 'https://scanner.tradingview.com';

export const tradingViewAPI = {
  // Get market data
  getMarketData: async (symbol) => {
    try {
      const response = await fetch(`${TRADINGVIEW_API_BASE}/vietnam/scan`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          filter: [
            {
              left: 'name',
              operation: 'match',
              right: symbol
            }
          ],
          columns: [
            'name',
            'close',
            'change',
            'change_abs',
            'volume',
            'market_cap_basic',
            'price_earnings_ttm',
            'earnings_per_share_basic_ttm',
            'number_of_employees',
            'sector',
            'industry',
            'market',
            'country',
            'exchange',
            'description',
            'type',
            'subtype',
            'update_mode',
            'pricescale',
            'minmov',
            'fractional',
            'minmove2'
          ],
          sort: {
            sortBy: 'market_cap_basic',
            sortOrder: 'desc'
          },
          range: [0, 50]
        })
      });
      
      return await response.json();
    } catch (error) {
      console.error('TradingView API Error:', error);
      throw error;
    }
  },

  // Get technical indicators
  getTechnicalIndicators: async (symbol) => {
    try {
      const response = await fetch(`${TRADINGVIEW_API_BASE}/vietnam/scan`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          filter: [
            {
              left: 'name',
              operation: 'match',
              right: symbol
            }
          ],
          columns: [
            'name',
            'close',
            'change',
            'change_abs',
            'volume',
            'RSI',
            'MACD.macd',
            'MACD.signal',
            'MACD.histogram',
            'EMA20',
            'EMA50',
            'SMA20',
            'SMA50',
            'BB.upper',
            'BB.lower',
            'BB.middle',
            'Stoch.K',
            'Stoch.D',
            'ADX',
            'CCI',
            'Williams',
            'ATR',
            'ADX',
            'CCI',
            'Williams',
            'ATR'
          ],
          sort: {
            sortBy: 'market_cap_basic',
            sortOrder: 'desc'
          },
          range: [0, 50]
        })
      });
      
      return await response.json();
    } catch (error) {
      console.error('TradingView Technical Indicators Error:', error);
      throw error;
    }
  },

  // Get market overview
  getMarketOverview: async () => {
    try {
      const response = await fetch(`${TRADINGVIEW_API_BASE}/vietnam/scan`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          filter: [
            {
              left: 'market_cap_basic',
              operation: 'nempty'
            }
          ],
          columns: [
            'name',
            'close',
            'change',
            'change_abs',
            'volume',
            'market_cap_basic',
            'sector',
            'industry'
          ],
          sort: {
            sortBy: 'market_cap_basic',
            sortOrder: 'desc'
          },
          range: [0, 100]
        })
      });
      
      return await response.json();
    } catch (error) {
      console.error('TradingView Market Overview Error:', error);
      throw error;
    }
  },

  // Get sector performance
  getSectorPerformance: async () => {
    try {
      const response = await fetch(`${TRADINGVIEW_API_BASE}/vietnam/scan`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          filter: [
            {
              left: 'sector',
              operation: 'nempty'
            }
          ],
          columns: [
            'sector',
            'close',
            'change',
            'change_abs',
            'volume',
            'market_cap_basic'
          ],
          sort: {
            sortBy: 'change_abs',
            sortOrder: 'desc'
          },
          range: [0, 50]
        })
      });
      
      return await response.json();
    } catch (error) {
      console.error('TradingView Sector Performance Error:', error);
      throw error;
    }
  }
};

export default tradingViewAPI;
