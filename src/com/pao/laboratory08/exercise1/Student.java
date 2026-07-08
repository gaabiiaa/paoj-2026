package com.pao.laboratory08.exercise1;

public class Student implements Cloneable {
    private String nume;
    private int varsta;
    private Adresa adresa;

    public Student(String nume, int varsta, Adresa adresa) {
        this.nume = nume;
        this.varsta = varsta;
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", adresa=" + adresa +
                '}';
    }

    // Shallow clone - modificările la obiectele referite se reflectă și în original
    public Object cloneShallow() throws CloneNotSupportedException {
        return super.clone();
    }

    // Deep clone - copii independente ale obiectelor referite
    @Override
    public Object clone() throws CloneNotSupportedException {
        Student clona = (Student) super.clone();
        // Deep clone: creează o copie independentă a adresei
        clona.adresa = (Adresa) this.adresa.clone();
        return clona;
    }
}
