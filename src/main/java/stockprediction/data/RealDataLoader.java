package stockprediction.data;

import stockprediction.model.StockData;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Load real OHLCV data from CSV into List<StockData>.
 * Supports typical Yahoo Finance CSV: Date,Open,High,Low,Close,Adj Close,Volume
 * Lines with invalid or missing values are skipped.
 */
public class RealDataLoader {

    public static List<StockData> loadFromCsv(Path csvPath, ZoneId zoneId) throws IOException {
        if (csvPath == null || !Files.exists(csvPath)) {
            return Collections.emptyList();
        }

        List<StockData> results = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header by detecting non-numeric tokens in price columns
                if (isFirst) {
                    isFirst = false;
                    if (line.toLowerCase().startsWith("date,")) {
                        continue;
                    }
                }

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                try {
                    String dateStr = parts[0].trim();
                    double open = parseDouble(parts[1]);
                    double high = parseDouble(parts[2]);
                    double low = parseDouble(parts[3]);
                    double close = parseDouble(parts[4]);
                    long volume = parseLongSafe(parts.length >= 7 ? parts[6] : parts[5]);

                    // Parse date (daily). If includes time, attempt LocalDateTime parse quickly
                    LocalDateTime timestamp;
                    if (dateStr.contains(":") || dateStr.contains(" ")) {
                        // Flexible: try ISO_LOCAL_DATE_TIME fallback to date-only
                        try {
                            timestamp = LocalDateTime.parse(dateStr.replace(' ', 'T'));
                        } catch (Exception e) {
                            LocalDate d = LocalDate.parse(dateStr.substring(0, 10));
                            timestamp = LocalDateTime.of(d, LocalTime.of(0, 0));
                        }
                    } else {
                        LocalDate d = LocalDate.parse(dateStr);
                        timestamp = LocalDateTime.of(d, LocalTime.of(0, 0));
                    }

                    if (close <= 0 || high <= 0 || low <= 0 || open <= 0) continue;
                    if (high < Math.max(open, close)) high = Math.max(open, close);
                    if (low > Math.min(open, close)) low = Math.min(open, close);

                    results.add(new StockData(timestamp, open, high, low, close, volume));
                } catch (Exception ignore) {
                    // skip invalid line
                }
            }
        }

        // Ensure ascending by time if CSV is newest-first
        results.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return results;
    }

    public static List<StockData> loadFromCsv(String csvPath, ZoneId zoneId) throws IOException {
        return loadFromCsv(Path.of(csvPath), zoneId);
    }

    private static double parseDouble(String s) {
        return Double.parseDouble(s.replace("\"", "").trim());
    }

    private static long parseLongSafe(String s) {
        try {
            return Long.parseLong(s.replace("\"", "").trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}


