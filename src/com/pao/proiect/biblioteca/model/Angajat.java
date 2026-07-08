package com.pao.proiect.biblioteca.model;

public class Angajat extends Persoana {
    private String departament;

    public Angajat(String id, String nume, String email, String departament) {
        super(id, nume, email);
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "Angajat";
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }
}
