package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

interface PlataOnline {
    void autentificare(String user, String parola);
    double consultareSold();
    boolean efectuarePlata(double suma);

    /**
     Daca cineva forțează apelul pe un obiect PlataOnline care nu suporta SMS, se va executa acest cod.
     */
    default boolean trimiteSMS(String mesaj) {
        throw new UnsupportedOperationException("Eroare: Această entitate nu are capabilitatea de a trimite SMS.");
    }
}

interface PlataOnlineSMS extends PlataOnline {
    // Suprascriem metoda pentru a o transforma din `default` inapoi in `abstracta`.
    boolean trimiteSMS(String mesaj);
}

/**
 * Folosim un Enum pt constante financiare deoarece ofera type safety si
  ne permite sa atasam valori si comportament specific fiecarei constante
 */
enum ConstanteFinanciare {
    TVA(0.19),
    SALARIU_MINIM(3700.0),
    COTA_IMPOZIT(0.10);

    private final double valoare;

    ConstanteFinanciare(double valoare) {
        this.valoare = valoare;
    }

    public double getValoare() {
        return valoare;
    }
}

abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected String telefon; //poate fi null

    public Persoana(String nume, String prenume, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }

    public String getNume() { return nume; }
    public String getTelefon() { return telefon; }
}

abstract class Angajat extends Persoana {
    protected double salariu;

    public Angajat(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }
}

/**
 * inginerul - o persoana fizica care poate face plati; fara SMS.
 * implementeaza comparable pt sortare .
 */
class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double soldCont;

    public Inginer(String nume, String prenume, String telefon, double salariu, double soldCont) {
        super(nume, prenume, telefon, salariu);
        this.soldCont = soldCont;
    }

    @Override
    public void autentificare(String user, String parola) {
        // edge case pt input invalid
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("Userul sau parola nu pot fi nule sau goale.");
        }
        System.out.println("Inginerul " + nume + " s-a autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        return soldCont;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) throw new IllegalArgumentException("Suma trebuie să fie pozitivă.");
        if (soldCont >= suma) {
            soldCont -= suma;
            System.out.println("Plată efectuată. Sold rămas: " + soldCont);
            return true;
        }
        return false;
    }

    /**
      sortarea alfabetica, dupa nume.
     */
    @Override
    public int compareTo(Inginer altul) {
        return this.nume.compareTo(altul.nume);
    }

    @Override
    public String toString() {
        return String.format("Inginer: %s %s | Salariu: %.2f", nume, prenume, salariu);
    }
}

/**
  PersoanaJuridica - poate primi confirmari prin SMS.
 */
class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double soldCont;
    private final List<String> smsTrimise; // Agregare pentru a păstra istoricul mesajelor

    public PersoanaJuridica(String nume, String prenume, String telefon, double soldCont) {
        super(nume, prenume, telefon);
        this.soldCont = soldCont;
        //  Inițializam lista in constructor pt a preveni NullPointerException
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("User/parola invalide.");
        }
        System.out.println("Firma " + nume + " autentificată.");
    }

    @Override
    public double consultareSold() {
        return soldCont;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) throw new IllegalArgumentException("Suma trebuie să fie pozitivă.");
        if (soldCont >= suma) {
            soldCont -= suma;
            return true;
        }
        return false;
    }

    /**
     * Tratarea edge case-urilor legate de numere de telefon lipsa sau mesaje invalide.
     */
    @Override
    public boolean trimiteSMS(String mesaj) {
        if (telefon == null || telefon.trim().isEmpty()) {
            System.out.println("-> [Eșec SMS] Entitatea " + nume + " nu are număr de telefon setat.");
            return false;
        }
        if (mesaj == null || mesaj.trim().isEmpty()) {
            System.out.println("-> [Eșec SMS] Mesajul este null sau gol.");
            return false;
        }

        // daca a trecut de validari, salvam mesajul
        smsTrimise.add(mesaj);
        System.out.println("-> [Succes SMS] Trimis către " + telefon + ": " + mesaj);
        return true;
    }

    public void afiseazaIstoric() {
        System.out.println("Istoric SMS pentru " + nume + ": " + smsTrimise);
    }
}

/**
  Comparator pt a sorta inginerii descrescator dupa salariu,.
 */
class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer i1, Inginer i2) {
        return Double.compare(i2.getSalariu(), i1.getSalariu()); // i2 vs i1 = descrescător
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("===  SORTARE INGINERI ===");
        Inginer[] ingineri = {
                new Inginer("Zaharia", "Ion", "0720000001", 6000, 1000),
                new Inginer("Avram", "Vasile", "0720000002", 8500, 2000),
                new Inginer("Dumitru", "Ana", null, 4500, 500)
        };

        Arrays.sort(ingineri);
        System.out.println("Sortare naturală (Alfabetic):");
        for (Inginer i : ingineri) System.out.println(i);

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare alternativă (Salariu descrescător):");
        for (Inginer i : ingineri) System.out.println(i);


        System.out.println("\n===  POLIMORFISM ȘI INTERFEȚE ===");
        PlataOnline plataSimpla = new Inginer("Popa", "Dan", "0700111222", 5000, 2000);
        plataSimpla.autentificare("dan", "123");
        plataSimpla.efectuarePlata(500);

        PlataOnlineSMS plataCuSms = new PersoanaJuridica("TechCorp SRL", "", "0799888777", 10000);
        plataCuSms.autentificare("admin", "pass");
        plataCuSms.trimiteSMS("Confirmare plată 100 RON.");


        System.out.println("\n===  EDGE CASES & TRATAREA ERORILOR ===");

        // Entitate valida care suporta SMS, dar nu are numar de telefon completat
        System.out.println("Test: Trimitere SMS către firmă fără telefon:");
        PersoanaJuridica firmaFaraTelefon = new PersoanaJuridica("Ghost SRL", "", null, 5000);
        boolean statusSms = firmaFaraTelefon.trimiteSMS("Acest SMS va pica."); // Va returna false

        //  Autentificare cu user null
        System.out.println("\nTest: Autentificare cu credentiale goale:");
        try {
            plataSimpla.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("-> [Excepție prinsă corect]: " + e.getMessage());
        }

        //  Fortarea trimiterii unui SMS de pe o entitate (Inginer) care nu are aceasta opțiune
        System.out.println("\nTest: Trimitere SMS de la un simplu Inginer:");
        try {
            plataSimpla.trimiteSMS("Salut!");
        } catch (UnsupportedOperationException e) {
            System.out.println("-> [Excepție prinsă corect]: " + e.getMessage());
        }

        System.out.println("\n=== CONSTANTE FINANCIARE ===");
        System.out.println("Preluare constantă din Enum: TVA = " + (ConstanteFinanciare.TVA.getValoare() * 100) + "%");
    }
}