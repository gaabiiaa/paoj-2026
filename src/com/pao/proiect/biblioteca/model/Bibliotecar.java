package com.pao.proiect.biblioteca.model;

public class Bibliotecar extends Angajat {
    public Bibliotecar(String id, String nume, String email, String departament) {
        super(id, nume, email, departament);
    }

    @Override
    public String getRol() {
        return "Bibliotecar";
    }
}
