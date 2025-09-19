package main.stockprediction.ui;

import main.stockprediction.model.StockData;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lightweight candlestick chart using pure Swing/Java2D (no external deps).
 */
public class CandlestickChart {

    public static void showCandles(String title, List<StockData> data) {
        if (data == null || data.isEmpty()) return;

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        CandlePanel panel = new CandlePanel(data);
        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class CandlePanel extends JPanel {
        private final List<StockData> data;
        private final int padding = 50;
        private final int candleWidth = 8;
        private final int candleGap = 4;

        CandlePanel(List<StockData> data) {
            this.data = data;
            int width = Math.max(900, padding * 2 + data.size() * (candleWidth + candleGap));
            setPreferredSize(new Dimension(width, 500));
            setBackground(Color.white);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Compute min/max for scaling
            double minPrice = Double.MAX_VALUE;
            double maxPrice = -Double.MAX_VALUE;
            for (StockData d : data) {
                minPrice = Math.min(minPrice, d.getLow());
                maxPrice = Math.max(maxPrice, d.getHigh());
            }
            // Add small margin
            double margin = (maxPrice - minPrice) * 0.05;
            minPrice -= margin;
            maxPrice += margin;

            int w = getWidth();
            int h = getHeight();

            // Axes
            int x0 = padding;
            int y0 = h - padding;
            int x1 = w - padding;
            int y1 = padding;

            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(x0, y0, x1, y0); // X axis
            g2.drawLine(x0, y0, x0, y1); // Y axis

            // Y grid/labels (5 ticks)
            g2.setColor(new Color(230, 230, 230));
            int ticks = 5;
            for (int i = 1; i <= ticks; i++) {
                int y = y0 - i * (y0 - y1) / ticks;
                g2.drawLine(x0, y, x1, y);
            }

            // Labels
            g2.setColor(Color.BLACK);
            for (int i = 0; i <= ticks; i++) {
                double price = minPrice + (maxPrice - minPrice) * i / ticks;
                int y = y0 - (int) ((price - minPrice) / (maxPrice - minPrice) * (y0 - y1));
                String label = String.format("%.2f", price);
                g2.drawString(label, 10, y + 4);
            }

            // X mapping by index (uniform spacing) to keep it simple
            int availableWidth = x1 - x0;
            int step = candleWidth + candleGap;
            int maxCandles = Math.max(1, availableWidth / step);
            int startIndex = Math.max(0, data.size() - maxCandles);

            // Draw candles
            for (int i = startIndex; i < data.size(); i++) {
                StockData d = data.get(i);

                int idx = i - startIndex;
                int cx = x0 + idx * step + candleGap / 2;

                int yHigh = priceToY(d.getHigh(), h, y0, y1, minPrice, maxPrice);
                int yLow = priceToY(d.getLow(), h, y0, y1, minPrice, maxPrice);
                int yOpen = priceToY(d.getOpen(), h, y0, y1, minPrice, maxPrice);
                int yClose = priceToY(d.getClose(), h, y0, y1, minPrice, maxPrice);

                boolean up = d.getClose() >= d.getOpen();
                Color bodyColor = up ? new Color(0, 158, 115) : new Color(213, 94, 94);
                Color outline = bodyColor.darker();

                // Wick
                g2.setColor(outline);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(cx + candleWidth / 2, yHigh, cx + candleWidth / 2, yLow);

                // Body
                int top = Math.min(yOpen, yClose);
                int bodyH = Math.max(1, Math.abs(yClose - yOpen));
                g2.setColor(bodyColor);
                g2.fillRect(cx, top, candleWidth, bodyH);
                g2.setColor(outline);
                g2.drawRect(cx, top, candleWidth, bodyH);
            }

            // Simple time labels (first and last)
            g2.setColor(Color.GRAY);
            LocalDateTime firstTs = data.get(Math.max(0, startIndex)).getTimestamp();
            LocalDateTime lastTs = data.get(data.size() - 1).getTimestamp();
            String firstLabel = firstTs.toLocalDate().toString();
            String lastLabel = lastTs.toLocalDate().toString();
            g2.drawString(firstLabel, x0, y0 + 20);
            g2.drawString(lastLabel, Math.max(x0 + 60, x1 - 80), y0 + 20);
        }

        private int priceToY(double price, int height, int y0, int y1, double min, double max) {
            double norm = (price - min) / (max - min);
            norm = Math.max(0.0, Math.min(1.0, norm));
            return y0 - (int) (norm * (y0 - y1));
        }
    }
}


