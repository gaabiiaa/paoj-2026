package com.pao.laboratory05.audit;
import java.util.Scanner;
/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            if (optiune == 0) {
                System.out.println("La revedere!");
                break;
            }

            switch (optiune) {
                case 1:
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine();
                    System.out.print("Nume departament: ");
                    String numeDept = scanner.nextLine();
                    System.out.print("Locație departament: ");
                    String locatieDept = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();

                    Departament dept = new Departament(numeDept, locatieDept);
                    Angajat angajat = new Angajat(nume, dept, salariu);
                    service.addAngajat(angajat);
                    break;
                case 2:
                    System.out.println("\n--- Angajați sortați după salariu ---");
                    service.listBySalary();
                    break;
                case 3:
                    System.out.print("Introduceți numele departamentului: ");
                    String searchDept = scanner.nextLine();
                    System.out.println("\n--- Angajați în departamentul " + searchDept + " ---");
                    service.findByDepartament(searchDept);
                    break;
                case 4:
                    service.printAuditLog();
                    break;
                default:
                    System.out.println("Opțiune invalidă!");
            }
        }
        scanner.close();
    }
}
