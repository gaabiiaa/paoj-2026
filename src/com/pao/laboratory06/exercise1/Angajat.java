package com.pao.laboratory06.exercise1;

import java.util.Scanner;
import java.util.Comparator;

public class Angajat {
    private String nume;
    private double salariu;

    public Angajat(String nume, double salariu) {
        this.nume = nume;
        this.salariu = salariu;
    }

    public static Angajat citeste(Scanner s) {
        String nume = s.next();
        double salariu = s.nextDouble();
        return new Angajat(nume, salariu);
    }

    @Override
    public String toString() {
        return String.format("%s %.1f", nume, salariu);
    }

    public String getNume() {
        return nume;
    }

    public double getSalariu() {
        return salariu;
    }

    public int compareTo(Angajat altAngajat) {
        return Double.compare(this.salariu, altAngajat.salariu);
    }
}
