package com.easemytrade.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and validates signal-related values extracted from page text.
 */
public class SignalParser {

    private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*%");
    private static final Pattern LEVEL_PATTERN = Pattern.compile("[\\d,]+(?:\\.\\d+)?");
    private static final Pattern PRICE_PATTERN = Pattern.compile("([\\d,]+(?:\\.\\d+)?)");

    public static final List<String> VALID_SIGNAL_STATES =
            Arrays.asList("Trade Signal", "Watchlist Only", "No Trade", "Hold", "Pending", "No trade");

    public static double parsePercentage(String text) {
        if (text == null || text.isBlank()) return -1;
        Matcher m = PERCENTAGE_PATTERN.matcher(text);
        if (m.find()) return Double.parseDouble(m.group(1));
        // Maybe plain number like "68"
        try { return Double.parseDouble(text.trim().replace("%", "")); }
        catch (NumberFormatException ignored) { return -1; }
    }

    public static double parsePrice(String text) {
        if (text == null || text.isBlank() || text.equals("--")) return -1;
        String cleaned = text.replaceAll("[,₹\\s]", "");
        try { return Double.parseDouble(cleaned); }
        catch (NumberFormatException ignored) { return -1; }
    }

    public static boolean isValidSignalState(String state) {
        if (state == null) return false;
        return VALID_SIGNAL_STATES.stream()
                .anyMatch(s -> s.equalsIgnoreCase(state.trim()));
    }

    public static boolean isConfidenceInRange(double confidence) {
        return confidence >= 0 && confidence <= 100;
    }

    /**
     * Validates that stop loss is below entry for a buy signal.
     * Entry > StopLoss and Target > Entry gives positive RR.
     */
    public static boolean isValidBuySetup(double entry, double stopLoss, double target) {
        return entry > 0 && stopLoss > 0 && target > 0
                && stopLoss < entry
                && target > entry;
    }

    /**
     * Validates that stop loss is above entry for a sell signal.
     */
    public static boolean isValidSellSetup(double entry, double stopLoss, double target) {
        return entry > 0 && stopLoss > 0 && target > 0
                && stopLoss > entry
                && target < entry;
    }

    /**
     * Calculates risk-reward ratio: (target - entry) / (entry - stop).
     * Must be >= 2.2 per the engine rules.
     */
    public static double calcRiskRewardRatio(double entry, double stopLoss, double target) {
        double risk = Math.abs(entry - stopLoss);
        if (risk == 0) return 0;
        return Math.abs(target - entry) / risk;
    }

    /**
     * For a valid index zone, support levels should all be below resistance levels.
     */
    public static boolean supportBelowResistance(double support, double resistance) {
        return support > 0 && resistance > 0 && support < resistance;
    }

    private SignalParser() {}
}
