package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public class Carte implements Comparable<Carte> {
    private final String id;
    private String titlu;
    private Autor autor;
    private String categorie;
    private boolean disponibila;
    private int numarImprumuturi;

    public Carte(String id, String titlu, Autor autor, String categorie, boolean disponibila) {
        this.id = id;
        this.titlu = titlu;
        this.autor = autor;
        this.categorie = categorie;
        this.disponibila = disponibila;
    }

    public String getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isDisponibila() {
        return disponibila;
    }

    public void setDisponibila(boolean disponibila) {
        this.disponibila = disponibila;
    }

    public int getNumarImprumuturi() {
        return numarImprumuturi;
    }

    public void setNumarImprumuturi(int numarImprumuturi) {
        this.numarImprumuturi = numarImprumuturi;
    }

    @Override
    public int compareTo(Carte other) {
        int byTitle = this.titlu.compareToIgnoreCase(other.titlu);
        if (byTitle != 0) {
            return byTitle;
        }
        return this.id.compareToIgnoreCase(other.id);
    }

    @Override
    public String toString() {
        return "Carte{" +
                "id='" + id + '\'' +
                ", titlu='" + titlu + '\'' +
                ", autor=" + autor.getNume() +
                ", categorie='" + categorie + '\'' +
                ", disponibila=" + disponibila +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carte)) return false;
        Carte carte = (Carte) o;
        return Objects.equals(id, carte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
