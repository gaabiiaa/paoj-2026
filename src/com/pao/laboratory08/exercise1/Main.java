package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

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
        
        // Citește comanda din stdin
        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();
        
        if (comanda.equals("PRINT")) {
            // Afișează toți studenții
            for (Student student : studenti) {
                System.out.println(student);
            }
        } else if (comanda.startsWith("SHALLOW ")) {
            // Shallow clone
            String nume = comanda.substring("SHALLOW ".length()).trim();
            Student original = findStudentByName(studenti, nume);
            
            if (original != null) {
                Student clona = (Student) original.cloneShallow();
                clona.getAdresa().setOras("MODIFICAT");
                
                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            }
        } else if (comanda.startsWith("DEEP ")) {
            // Deep clone
            String nume = comanda.substring("DEEP ".length()).trim();
            Student original = findStudentByName(studenti, nume);
            
            if (original != null) {
                Student clona = (Student) original.clone();
                clona.getAdresa().setOras("MODIFICAT");
                
                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            }
        }
    }
    
    private static Student findStudentByName(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }
        return null;
    }
}
