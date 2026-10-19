package ru.kudryaeva.subscriptions.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class MonthFormat {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    private MonthFormat() {
    }

    public static LocalDate parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("invalid date format, use MM-YYYY");
        }
        try {
            return YearMonth.parse(value.trim(), FORMAT).atDay(1);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("invalid date format, use MM-YYYY");
        }
    }

    public static String format(LocalDate date) {
        return date.format(FORMAT);
    }
}
