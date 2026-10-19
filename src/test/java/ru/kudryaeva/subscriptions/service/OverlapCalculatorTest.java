package ru.kudryaeva.subscriptions.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OverlapCalculatorTest {

    @Test
    void countsInclusiveMonthsInsidePeriod() {
        int months = OverlapCalculator.overlapMonths(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 12, 1)
        );
        assertEquals(6, months);
    }

    @Test
    void returnsZeroWhenRangesDoNotOverlap() {
        int months = OverlapCalculator.overlapMonths(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 12, 1)
        );
        assertEquals(0, months);
    }
}
