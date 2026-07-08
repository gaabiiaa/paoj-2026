package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.Sectiune;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectiuneService {
    private static SectiuneService instance;
    private final Map<String, Sectiune> sectiuniById = new HashMap<>();
    private final AuditService auditService = AuditService.getInstance();

    private SectiuneService() {
    }

    public static SectiuneService getInstance() {
        if (instance == null) {
            instance = new SectiuneService();
        }
        return instance;
    }

    public void adaugaSectiune(Sectiune sectiune) {
        if (sectiune == null) {
            throw new IllegalArgumentException("Sectiune invalida");
        }
        auditService.logAction("adauga_sectiune");
        sectiuniById.put(sectiune.getId(), sectiune);
    }

    public List<Sectiune> toateSectiunile() {
        auditService.logAction("listeaza_sectiuni");
        return new ArrayList<>(sectiuniById.values());
    }

    public Sectiune cautaSectiune(String id) {
        auditService.logAction("cauta_sectiune");
        return sectiuniById.get(id);
    }

    public void stergeSectiune(String id) {
        auditService.logAction("sterge_sectiune");
        sectiuniById.remove(id);
    }

    public List<Carte> cartiDinSectiune(String sectiuneId) {
        auditService.logAction("carti_din_sectiune");
        return sectiuniById.getOrDefault(sectiuneId, new Sectiune("", "")).getCarti();
    }
}
