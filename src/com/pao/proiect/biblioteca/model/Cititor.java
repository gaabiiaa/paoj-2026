package com.pao.proiect.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

public class Cititor extends Persoana {
    private final List<Imprumut> istoriculImprumuturilor = new ArrayList<>();
    private boolean activ = true;

    public Cititor(String id, String nume, String email) {
        super(id, nume, email);
    }

    @Override
    public String getRol() {
        return "Cititor";
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public List<Imprumut> getIstoriculImprumuturilor() {
        return istoriculImprumuturilor;
    }

    public void adaugaImprumut(Imprumut imprumut) {
        istoriculImprumuturilor.add(imprumut);
    }
}
