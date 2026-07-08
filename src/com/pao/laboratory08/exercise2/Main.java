package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) throws Exception {
        // Citește studenții din FILE_PATH
        List<Student> studenti = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (linie.trim().isEmpty()) {
                    continue;
                }
                String[] parti = linie.split(",");
                if (parti.length == 4) {
                    String nume = parti[0].trim();
                    int varsta = Integer.parseInt(parti[1].trim());
                    String oras = parti[2].trim();
                    String strada = parti[3].trim();
                    
                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);
                    studenti.add(student);
                }
            }
        }
        
        // Citește pragul de vârstă din stdin
        Scanner scanner = new Scanner(System.in);
        int prag = Integer.parseInt(scanner.nextLine().trim());
        
        // Filtrează studenții cu varsta >= prag
        List<Student> filtrati = new ArrayList<>();
        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                filtrati.add(student);
            }
        }
        
        // Afișează filtrul și numărul de rezultate
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        
        // Afișează studenții filtrați
        for (Student student : filtrati) {
            System.out.println(student);
        }
        
        // Scrie studenții filtrați în fișier
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Student student : filtrati) {
                bw.write(student.toString());
                bw.newLine();
            }
        }
        
        // Afișează mesajul final
        System.out.println();
        System.out.println("Scris in: " + OUTPUT_FILE);
    }
}

