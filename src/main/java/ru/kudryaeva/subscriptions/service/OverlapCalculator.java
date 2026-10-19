package ru.kudryaeva.subscriptions.service;

import java.time.LocalDate;

public final class OverlapCalculator {

    private OverlapCalculator() {
    }

    public static int overlapMonths(LocalDate aStart, LocalDate aEnd, LocalDate bStart, LocalDate bEnd) {
        LocalDate start = aStart.isAfter(bStart) ? aStart : bStart;
        LocalDate end = aEnd.isBefore(bEnd) ? aEnd : bEnd;
        if (end.isBefore(start)) {
            return 0;
        }
        int months = 0;
        LocalDate cur = start;
        while (!cur.isAfter(end)) {
            months++;
            cur = cur.plusMonths(1);
        }
        return months;
    }
}
