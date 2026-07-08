package com.pao.proiect.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

public class Sectiune {
    private final String id;
    private String nume;
    private final List<Carte> carti = new ArrayList<>();

    public Sectiune(String id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public String getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Carte> getCarti() {
        return carti;
    }

    public void adaugaCarte(Carte carte) {
        carti.add(carte);
    }
}
