package com.pao.laboratory06.exercise2;

import java.util.*;

enum TipColaborator { CIM, PFA, SRL }

interface IOperatiiCitireScriere {
    void citeste(Scanner in);
    void afiseaza();
    String tipContract();
    default boolean areBonus() { return false; }
}

interface PersoanaFizica {}
interface PersoanaJuridica {}

abstract class Colaborator implements IOperatiiCitireScriere, Comparable<Colaborator> {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;
    protected TipColaborator tip;

    public abstract double calculeazaVenitNetAnual();

    @Override
    public int compareTo(Colaborator alt) {
        return Double.compare(alt.calculeazaVenitNetAnual(), this.calculeazaVenitNetAnual());
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    public TipColaborator getTip() {
        return tip;
    }

    @Override
    public String tipContract() {
        return tip.name();
    }
}

class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus = false;

    public CIMColaborator() {
        this.tip = TipColaborator.CIM;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        if (in.hasNext()) {
            String b = in.next();
            if (b.equalsIgnoreCase("DA")) {
                this.bonus = true;
            }
        }
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (areBonus()) {
            net += net * 0.10;
        }
        return net;
    }

    @Override
    public void afiseaza() {
        System.out.printf( "%s: %s, venit net anual: %.2f lei\n", tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }
}

class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM_BRUT = 48600.0;

    public PFAColaborator() {
        this.tip = TipColaborator.PFA;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInitial = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNetInitial;

        // Calcul CASS
        double cass = 0;
        if (venitNetInitial < 6 * SALARIU_MINIM_BRUT) {
            cass = 0.10 * (6 * SALARIU_MINIM_BRUT);
        } else if (venitNetInitial <= 72 * SALARIU_MINIM_BRUT) {
            cass = 0.10 * venitNetInitial;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_BRUT);
        }

        // Calcul CAS
        double cas = 0;
        if (venitNetInitial >= 12 * SALARIU_MINIM_BRUT && venitNetInitial <= 24 * SALARIU_MINIM_BRUT) {
            cas = 0.25 * (12 * SALARIU_MINIM_BRUT);
        } else if (venitNetInitial > 24 * SALARIU_MINIM_BRUT) {
            cas = 0.25 * (24 * SALARIU_MINIM_BRUT);
        }

        return venitNetInitial - impozit - cass - cas;
    }

    @Override
    public void afiseaza() {
        System.out.printf( "%s: %s, venit net anual: %.2f lei\n", tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }
}

class SRLColaborator extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    public SRLColaborator() {
        this.tip = TipColaborator.SRL;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitBrutLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void afiseaza() {
        System.out.printf( "%s: %s, venit net anual: %.2f lei\n", tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }
}

//  main
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        scanner.nextLine();

        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) { i--; continue; }

            Scanner lineScanner = new Scanner(line);
            String tip = lineScanner.next();
            Colaborator colab = null;

            switch (tip) {
                case "CIM": colab = new CIMColaborator(); break;
                case "PFA": colab = new PFAColaborator(); break;
                case "SRL": colab = new SRLColaborator(); break;
            }

            if (colab != null) {
                colab.citeste(lineScanner);
                colaboratori.add(colab);
            }
        }

        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }
        System.out.println();

        Collections.sort(colaboratori);

        if (!colaboratori.isEmpty()) {
            System.out.print("Colaborator cu venit net maxim: ");
            colaboratori.get(0).afiseaza();
        }
        System.out.println();


        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }
        System.out.println();


        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tipEnum : TipColaborator.values()) {
            double suma = 0;
            int numar = 0;
            for (Colaborator c : colaboratori) {
                if (c.getTip() == tipEnum) {
                    suma += c.calculeazaVenitNetAnual();
                    numar++;
                }
            }
            if (numar == 0) {
                System.out.printf( "%s: suma = nu lei, număr = null\n", tipEnum.name());
            } else {
                System.out.printf( "%s: suma = %.2f lei, număr = %d\n", tipEnum.name(), suma, numar);
            }
        }

        scanner.close();
    }
}