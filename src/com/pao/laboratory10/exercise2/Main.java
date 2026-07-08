package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n && scanner.hasNext(); i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "UNIQUE_IDS" -> {
                    LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                    for (Tranzactie tranzactie : tranzactii) {
                        uniqueIds.add(tranzactie.getId());
                    }
                    System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);
                }
                case "MONTHLY_REPORT" -> {
                    TreeMap<String, double[]> report = new TreeMap<>();
                    for (Tranzactie tranzactie : tranzactii) {
                        String month = tranzactie.getData().substring(0, 7);
                        report.putIfAbsent(month, new double[2]);
                        double[] sums = report.get(month);
                        if (tranzactie.getTip() == TipTranzactie.CREDIT) {
                            sums[0] += tranzactie.getSuma();
                        } else {
                            sums[1] += tranzactie.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> entry : report.entrySet()) {
                        double[] sums = entry.getValue();
                        System.out.println(String.format(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON", entry.getKey(), sums[0], sums[1]));
                    }
                }
                case "TOP" -> {
                    if (!scanner.hasNextInt()) {
                        break;
                    }
                    int topN = scanner.nextInt();
                    List<Tranzactie> sorted = new ArrayList<>(tranzactii);
                    sorted.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    System.out.println("Top " + topN + ":");
                    int limit = Math.min(topN, sorted.size());
                    for (int i = 0; i < limit; i++) {
                        System.out.println(sorted.get(i));
                    }
                }
                case "SORT_ASC" -> {
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie tranzactie : tranzactii) {
                        System.out.println(tranzactie);
                    }
                }
                case "SORT_DESC" -> {
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie tranzactie : tranzactii) {
                        System.out.println(tranzactie);
                    }
                }
                case "REVERSE" -> {
                    Collections.reverse(tranzactii);
                    for (Tranzactie tranzactie : tranzactii) {
                        System.out.println(tranzactie);
                    }
                }
                case "MIN_MAX" -> {
                    if (!tranzactii.isEmpty()) {
                        Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                        Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                        System.out.println("MIN: " + min);
                        System.out.println("MAX: " + max);
                    }
                }
                case "CME_DEMO" -> {
                    try {
                        for (Tranzactie tranzactie : tranzactii) {
                            tranzactii.remove(tranzactie);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                }
                default -> {
                    // ignore unknown commands
                }
            }
        }
    }
}
