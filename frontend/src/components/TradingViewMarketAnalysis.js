import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { TrendingUp, TrendingDown, BarChart3, Activity } from 'lucide-react';
import tradingViewAPI from '../services/tradingViewAPI';

const TradingViewMarketAnalysis = () => {
  const [marketData, setMarketData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedSymbol, setSelectedSymbol] = useState('VN-INDEX');

  useEffect(() => {
    loadMarketData();
  }, []);

  const loadMarketData = async () => {
    try {
      setLoading(true);
      const data = await tradingViewAPI.getMarketOverview();
      setMarketData(data.data || []);
    } catch (err) {
      setError('Không thể tải dữ liệu từ TradingView');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getSymbolData = async (symbol) => {
    try {
      setLoading(true);
      const data = await tradingViewAPI.getMarketData(symbol);
      return data.data || [];
    } catch (err) {
      setError(`Không thể tải dữ liệu cho ${symbol}`);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const getTechnicalIndicators = async (symbol) => {
    try {
      const data = await tradingViewAPI.getTechnicalIndicators(symbol);
      return data.data || [];
    } catch (err) {
      console.error('Technical indicators error:', err);
      return [];
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
      minimumFractionDigits: 2,
    }).format(price);
  };

  const formatPercentage = (percentage) => {
    return `${percentage > 0 ? '+' : ''}${percentage.toFixed(2)}%`;
  };

  const getChangeColor = (change) => {
    return change > 0 ? 'text-green-500' : change < 0 ? 'text-red-500' : 'text-gray-500';
  };

  const getChangeIcon = (change) => {
    return change > 0 ? <TrendingUp className="w-4 h-4" /> : change < 0 ? <TrendingDown className="w-4 h-4" /> : null;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-lg p-4">
        <p className="text-red-600">{error}</p>
        <button 
          onClick={loadMarketData}
          className="mt-2 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
        >
          Thử lại
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Phân Tích Thị Trường</h1>
            <p className="text-gray-600">Dữ liệu từ TradingView</p>
          </div>
          <div className="flex items-center space-x-2">
            <Activity className="w-6 h-6 text-blue-500" />
            <span className="text-sm text-gray-500">Real-time</span>
          </div>
        </div>
      </div>

      {/* Market Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {marketData.slice(0, 4).map((stock, index) => (
          <div key={index} className="bg-white rounded-lg shadow-sm border p-4">
            <div className="flex items-center justify-between mb-2">
              <h3 className="font-semibold text-gray-900">{stock.name}</h3>
              <div className={`flex items-center space-x-1 ${getChangeColor(stock.change)}`}>
                {getChangeIcon(stock.change)}
                <span className="text-sm font-medium">
                  {formatPercentage(stock.change)}
                </span>
              </div>
            </div>
            <div className="space-y-1">
              <p className="text-lg font-bold text-gray-900">
                {formatPrice(stock.close)}
              </p>
              <p className="text-sm text-gray-500">
                Volume: {new Intl.NumberFormat('vi-VN').format(stock.volume)}
              </p>
              {stock.sector && (
                <p className="text-xs text-gray-400">
                  {stock.sector}
                </p>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Market Table */}
      <div className="bg-white rounded-lg shadow-sm border">
        <div className="px-6 py-4 border-b">
          <h2 className="text-lg font-semibold text-gray-900">Danh Sách Cổ Phiếu</h2>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Mã
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Giá
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Thay Đổi
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Volume
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Vốn Hóa
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ngành
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {marketData.map((stock, index) => (
                <tr key={index} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{stock.name}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{formatPrice(stock.close)}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className={`flex items-center space-x-1 ${getChangeColor(stock.change)}`}>
                      {getChangeIcon(stock.change)}
                      <span className="text-sm font-medium">
                        {formatPercentage(stock.change)}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">
                      {new Intl.NumberFormat('vi-VN').format(stock.volume)}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">
                      {stock.market_cap_basic ? 
                        new Intl.NumberFormat('vi-VN', {
                          style: 'currency',
                          currency: 'VND',
                          notation: 'compact'
                        }).format(stock.market_cap_basic) : 'N/A'
                      }
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{stock.sector || 'N/A'}</div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Technical Analysis */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Phân Tích Kỹ Thuật</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {marketData.slice(0, 6).map((stock, index) => (
            <div key={index} className="border rounded-lg p-4">
              <h3 className="font-semibold text-gray-900 mb-2">{stock.name}</h3>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-sm text-gray-600">RSI:</span>
                  <span className="text-sm font-medium">{stock.RSI || 'N/A'}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-600">MACD:</span>
                  <span className="text-sm font-medium">{stock['MACD.macd'] || 'N/A'}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-600">EMA20:</span>
                  <span className="text-sm font-medium">{stock.EMA20 || 'N/A'}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-600">EMA50:</span>
                  <span className="text-sm font-medium">{stock.EMA50 || 'N/A'}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default TradingViewMarketAnalysis;
