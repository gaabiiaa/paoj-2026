package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "ENQUEUE" -> {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    coada.addLast(new Tranzactie(id, suma, data, tip));
                }
                case "PUSH" -> {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                }
                case "DEQUEUE" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie tranzactie = coada.removeFirst();
                        System.out.println("Procesat: " + tranzactie);
                    }
                }
                case "POP" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie tranzactie = coada.removeFirst();
                        System.out.println("Extras: " + tranzactie);
                    }
                }
                case "REMOVE_DEBIT" -> {
                    int count = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();
                    while (iterator.hasNext()) {
                        Tranzactie tranzactie = iterator.next();
                        if (tranzactie.getTip() == TipTranzactie.DEBIT) {
                            iterator.remove();
                            count++;
                        }
                    }
                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                }
                case "REMOVE_BELOW" -> {
                    double threshold = scanner.nextDouble();
                    int count = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();
                    while (iterator.hasNext()) {
                        Tranzactie tranzactie = iterator.next();
                        if (tranzactie.getSuma() < threshold) {
                            iterator.remove();
                            count++;
                        }
                    }
                    System.out.println(String.format(Locale.US, "Eliminat %d tranzactii sub %.2f RON.", count, threshold));
                }
                case "PRINT" -> {
                    for (Tranzactie tranzactie : coada) {
                        System.out.println(tranzactie);
                    }
                }
                case "SIZE" -> System.out.println("Dimensiune coada: " + coada.size());
                default -> {
                    // Ignore unrecognized commands
                }
            }
        }
    }
}
