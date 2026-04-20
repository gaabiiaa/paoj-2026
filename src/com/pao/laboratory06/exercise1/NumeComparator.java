package com.pao.laboratory06.exercise1;

import com.pao.laboratory06.exercise1.Angajat;

import java.util.Comparator;

public class NumeComparator implements Comparator<Angajat> {
    @Override
    public int compare(Angajat a1, Angajat a2) {
        return a1.getNume().compareTo(a2.getNume());
    }
}