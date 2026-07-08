package com.pao.proiect.biblioteca.model;

import java.time.LocalDate;

public class Imprumut {
    private final String id;
    private final Carte carte;
    private final Cititor cititor;
    private final LocalDate dataImprumutului;
    private LocalDate dataReturnarii;

    public Imprumut(String id, Carte carte, Cititor cititor, LocalDate dataImprumutului) {
        this.id = id;
        this.carte = carte;
        this.cititor = cititor;
        this.dataImprumutului = dataImprumutului;
    }

    public String getId() {
        return id;
    }

    public Carte getCarte() {
        return carte;
    }

    public Cititor getCititor() {
        return cititor;
    }

    public LocalDate getDataImprumutului() {
        return dataImprumutului;
    }

    public LocalDate getDataReturnarii() {
        return dataReturnarii;
    }

    public void setDataReturnarii(LocalDate dataReturnarii) {
        this.dataReturnarii = dataReturnarii;
    }

    @Override
    public String toString() {
        return "Imprumut{" +
                "id='" + id + '\'' +
                ", carte=" + carte.getTitlu() +
                ", cititor=" + cititor.getNume() +
                ", dataImprumutului=" + dataImprumutului +
                ", dataReturnarii=" + dataReturnarii +
                '}';
    }
}
