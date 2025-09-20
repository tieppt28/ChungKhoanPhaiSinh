package stockprediction.indicators;

import stockprediction.model.StockData;
import java.util.List;
import java.util.ArrayList;

/**
 * Technical indicators calculator for stock analysis
 */
public class TechnicalIndicators {

    /**
     * Calculate Simple Moving Average (SMA)
     */
    public static List<Double> calculateSMA(List<StockData> data, int period) {
        List<Double> sma = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (i < period - 1) {
                sma.add(null); // Not enough data points
                continue;
            }

            double sum = 0;
            for (int j = i - period + 1; j <= i; j++) {
                sum += data.get(j).getClose();
            }
            sma.add(sum / period);
        }

        return sma;
    }

    /**
     * Calculate Exponential Moving Average (EMA)
     */
    public static List<Double> calculateEMA(List<StockData> data, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);

        double smaSum = 0.0;
        for (int i = 0; i < data.size(); i++) {
            double close = data.get(i).getClose();

            if (i < period - 1) {
                // Not enough data to seed EMA
                smaSum += close;
                ema.add(null);
                continue;
            }

            if (i == period - 1) {
                // Seed EMA with SMA of first 'period' values
                smaSum += close;
                double sma = smaSum / period;
                ema.add(sma);
                continue;
            }

            double prevEma = ema.get(i - 1);
            double currentEMA = (close - prevEma) * multiplier + prevEma;
            ema.add(currentEMA);
        }

        return ema;
    }

    /**
     * Calculate Relative Strength Index (RSI)
     */
    public static List<Double> calculateRSI(List<StockData> data, int period) {
        List<Double> rsi = new ArrayList<>();

        double prevAvgGain = 0.0;
        double prevAvgLoss = 0.0;

        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                rsi.add(null);
                continue;
            }

            double change = data.get(i).getClose() - data.get(i - 1).getClose();
            double gain = Math.max(0.0, change);
            double loss = Math.max(0.0, -change);

            if (i < period) {
                // Not enough data to compute initial averages
                rsi.add(null);
                prevAvgGain += gain;
                prevAvgLoss += loss;
                if (i == period - 1) {
                    prevAvgGain /= period;
                    prevAvgLoss /= period;
                    double rs = prevAvgLoss == 0 ? Double.POSITIVE_INFINITY : prevAvgGain / prevAvgLoss;
                    double value = prevAvgLoss == 0 ? 100.0 : 100 - (100 / (1 + rs));
                    rsi.set(i, value);
                }
                continue;
            }

            // Wilder's smoothing
            prevAvgGain = (prevAvgGain * (period - 1) + gain) / period;
            prevAvgLoss = (prevAvgLoss * (period - 1) + loss) / period;

            double rs = prevAvgLoss == 0 ? Double.POSITIVE_INFINITY : prevAvgGain / prevAvgLoss;
            double value = prevAvgLoss == 0 ? 100.0 : 100 - (100 / (1 + rs));
            rsi.add(value);
        }

        return rsi;
    }

    /**
     * Calculate MACD (Moving Average Convergence Divergence)
     */
    public static MACDResult calculateMACD(List<StockData> data, int fastPeriod, int slowPeriod, int signalPeriod) {
        List<Double> fastEMA = calculateEMA(data, fastPeriod);
        List<Double> slowEMA = calculateEMA(data, slowPeriod);
        List<Double> macdLine = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (fastEMA.get(i) != null && slowEMA.get(i) != null) {
                macdLine.add(fastEMA.get(i) - slowEMA.get(i));
            } else {
                macdLine.add(null);
            }
        }

        // Signal line with SMA seed on the first 'signalPeriod' valid MACD values
        List<Double> signalLine = calculateEMAFromValues(macdLine, signalPeriod);

        List<Double> histogram = new ArrayList<>();
        for (int i = 0; i < macdLine.size(); i++) {
            if (macdLine.get(i) != null && signalLine.get(i) != null) {
                histogram.add(macdLine.get(i) - signalLine.get(i));
            } else {
                histogram.add(null);
            }
        }

        return new MACDResult(macdLine, signalLine, histogram);
    }

    /**
     * Helper method to calculate EMA from a list of values
     */
    private static List<Double> calculateEMAFromValues(List<Double> values, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);

        double smaSum = 0.0;
        int validCount = 0;

        for (int i = 0; i < values.size(); i++) {
            Double v = values.get(i);
            if (v == null) {
                ema.add(null);
                continue;
            }

            if (validCount < period) {
                smaSum += v;
                validCount++;
                if (validCount < period) {
                    ema.add(null);
                } else {
                    double sma = smaSum / period;
                    ema.add(sma);
                }
                continue;
            }

            double prevEma = ema.get(i - 1);
            double currentEMA = (v - prevEma) * multiplier + prevEma;
            ema.add(currentEMA);
        }

        return ema;
    }

    /**
     * MACD calculation result container
     */
    public static class MACDResult {
        private final List<Double> macdLine;
        private final List<Double> signalLine;
        private final List<Double> histogram;

        public MACDResult(List<Double> macdLine, List<Double> signalLine, List<Double> histogram) {
            this.macdLine = macdLine;
            this.signalLine = signalLine;
            this.histogram = histogram;
        }

        public List<Double> getMacdLine() { return macdLine; }
        public List<Double> getSignalLine() { return signalLine; }
        public List<Double> getHistogram() { return histogram; }
    }
}

