package com.pao.laboratory10.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    private enum TipTranzactie {
        CREDIT,
        DEBIT
    }

    private static class Tranzactie {
        private final int id;
        private final double suma;
        private final String data;
        private final TipTranzactie tip;
        private final String contSursa;

        public Tranzactie(int id, double suma, String data, TipTranzactie tip, String contSursa) {
            this.id = id;
            this.suma = suma;
            this.data = data;
            this.tip = tip;
            this.contSursa = contSursa;
        }

        public double getSuma() {
            return suma;
        }

        public String getData() {
            return data;
        }

        public TipTranzactie getTip() {
            return tip;
        }

        public String getContSursa() {
            return contSursa;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "[%d] %s %s: %.2f RON (%s)", id, data, tip, suma, contSursa);
        }
    }

    public static void main(String[] args) {
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, 1200.00, "2024-01-05", TipTranzactie.CREDIT, "CONT_A"),
                new Tranzactie(2, 450.50, "2024-01-10", TipTranzactie.DEBIT, "CONT_B"),
                new Tranzactie(3, 780.25, "2024-01-15", TipTranzactie.CREDIT, "CONT_C"),
                new Tranzactie(4, 230.00, "2024-02-02", TipTranzactie.DEBIT, "CONT_A"),
                new Tranzactie(5, 1600.00, "2024-02-18", TipTranzactie.CREDIT, "CONT_D"),
                new Tranzactie(6, 95.75, "2024-02-20", TipTranzactie.DEBIT, "CONT_B"),
                new Tranzactie(7, 350.00, "2024-03-03", TipTranzactie.CREDIT, "CONT_E"),
                new Tranzactie(8, 620.00, "2024-03-14", TipTranzactie.DEBIT, "CONT_A"),
                new Tranzactie(9, 1400.00, "2024-03-21", TipTranzactie.CREDIT, "CONT_F"),
                new Tranzactie(10, 270.00, "2024-03-28", TipTranzactie.DEBIT, "CONT_C")
        );

        System.out.println("1. Tranzactii CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n2. Total procesat:");
        double total = tranzactii.stream().mapToDouble(Tranzactie::getSuma).sum();
        System.out.println(String.format(Locale.US, "Total procesat: %.2f RON", total));

        System.out.println("\n3. Raport lunar:");
        Map<String, Double> monthlySum = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.getData().substring(0, 7), TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)));
        monthlySum.forEach((month, sum) -> System.out.println(String.format(Locale.US, "%s: %.2f RON", month, sum)));

        System.out.println("\n4. Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n5. Conturi sursa unice:");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(conturiUnice);

        System.out.println("\n6. Suma medie:");
        double average = tranzactii.stream().mapToDouble(Tranzactie::getSuma).average().orElse(0.0);
        System.out.println(String.format(Locale.US, "Suma medie: %.2f RON", average));

        System.out.println("\n7. EXTRAS DE CONT per luna:");
        Map<String, List<Tranzactie>> groupedByMonth = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.getData().substring(0, 7), TreeMap::new, Collectors.toList()));
        groupedByMonth.forEach((month, list) -> {
            double monthTotal = list.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.println(String.format(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON", month, list.size(), monthTotal));
        });
    }
}
