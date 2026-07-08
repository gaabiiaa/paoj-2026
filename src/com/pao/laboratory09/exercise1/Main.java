package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.hasNextInt() ? scanner.nextInt() : 0;
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(tranzactii);
        }

        List<Tranzactie> deserialized;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            deserialized = (List<Tranzactie>) in.readObject();
        }

        Map<Integer, Tranzactie> tranzactieMap = new HashMap<>();
        for (Tranzactie t : deserialized) {
            tranzactieMap.put(t.getId(), t);
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "LIST" -> {
                    for (Tranzactie t : deserialized) {
                        System.out.println(t);
                    }
                }
                case "FILTER" -> {
                    String prefix = scanner.next();
                    boolean found = false;
                    for (Tranzactie t : deserialized) {
                        if (t.getData().startsWith(prefix)) {
                            System.out.println(t);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("Niciun rezultat.");
                    }
                }
                case "NOTE" -> {
                    int id = scanner.nextInt();
                    Tranzactie t = tranzactieMap.get(id);
                    if (t != null) {
                        System.out.println("NOTE[" + id + "]: " + t.getNote());
                    } else {
                        System.out.println("NOTE[" + id + "]: not found");
                    }
                }
                default -> {
                    // ignore unknown commands
                }
            }
        }
    }
}
