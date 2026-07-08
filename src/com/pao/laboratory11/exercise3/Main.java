package com.pao.laboratory11.exercise3;

import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        List<Transaction> data = List.of(
                new Transaction(1, 200.00, "2026-05-01", "RO", "WEB"),
                new Transaction(2, 300.00, "2026-05-01", "RO", "ATM"),
                new Transaction(3, 50.00, "2026-05-10", "NL", "APP"),
                new Transaction(4, 90.00, "2026-06-02", "RO", "WEB"),
                new Transaction(5, 600.00, "2026-06-05", "NG", "CRYPTO")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        // Query 1: top transactions
        System.out.println("Top transactions:");
        snap.getTopTransactions().forEach(t -> System.out.println("  " + t));

        // Query 2: counts by country (descending print for clarity)
        System.out.println("Counts by country:");
        snap.getCountByCountry().entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.println(String.format(Locale.US, "  %s -> %d", e.getKey(), e.getValue())));

        // Query 3: channels ordered by count
        System.out.println("Channels by count:");
        snap.getCountByChannel().entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = Long.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    return a.getKey().compareTo(b.getKey());
                })
                .forEach(e -> System.out.println(String.format(Locale.US, "  %s -> %d", e.getKey(), e.getValue())));
    }
}
