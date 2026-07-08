package com.pao.proiect.biblioteca.model;

public class Autor extends Persoana {
    private String nationalitate;

    public Autor(String id, String nume, String email, String nationalitate) {
        super(id, nume, email);
        this.nationalitate = nationalitate;
    }

    @Override
    public String getRol() {
        return "Autor";
    }

    public String getNationalitate() {
        return nationalitate;
    }

    public void setNationalitate(String nationalitate) {
        this.nationalitate = nationalitate;
    }
}
