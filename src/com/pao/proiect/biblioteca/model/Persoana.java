package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public abstract class Persoana {
    private final String id;
    private String nume;
    private String email;

    protected Persoana(String id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public abstract String getRol();

    public String getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getRol() + "{" + "id='" + id + '\'' + ", nume='" + nume + '\'' + ", email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana)) return false;
        Persoana persoana = (Persoana) o;
        return Objects.equals(id, persoana.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
