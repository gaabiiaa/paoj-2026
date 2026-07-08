package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.exception.CarteNegasitaException;
import com.pao.proiect.biblioteca.exception.CarteNedisponibilaException;
import com.pao.proiect.biblioteca.model.Carte;

import java.util.*;

public class CarteService {
    private static CarteService instance;
    private final Map<String, Carte> cartiById = new HashMap<>();
    private final TreeSet<Carte> cartiSortate = new TreeSet<>();
    private final Map<String, List<Carte>> cartiByAutor = new HashMap<>();
    private final AuditService auditService = AuditService.getInstance();

    private CarteService() {
    }

    public static CarteService getInstance() {
        if (instance == null) {
            instance = new CarteService();
        }
        return instance;
    }

    public void adaugaCarte(Carte carte) {
        if (carte == null) {
            throw new IllegalArgumentException("Carte invalida");
        }
        auditService.logAction("adauga_carte");
        cartiById.put(carte.getId(), carte);
        cartiSortate.add(carte);
        cartiByAutor.computeIfAbsent(carte.getAutor().getNume(), k -> new ArrayList<>()).add(carte);
    }

    public Carte cautaCarte(String id) throws CarteNegasitaException {
        auditService.logAction("cauta_carte");
        Carte carte = cartiById.get(id);
        if (carte == null) {
            throw new CarteNegasitaException("Cartea cu id " + id + " nu a fost gasita");
        }
        return carte;
    }

    public void stergeCarte(String id) throws CarteNegasitaException {
        auditService.logAction("sterge_carte");
        Carte carte = cautaCarte(id);
        cartiById.remove(id);
        cartiSortate.remove(carte);
        cartiByAutor.values().forEach(list -> list.remove(carte));
    }

    public List<Carte> toateCartile() {
        auditService.logAction("listeaza_carti");
        return new ArrayList<>(cartiSortate);
    }

    public List<Carte> cautaDupaAutor(String autorNume) {
        auditService.logAction("cauta_carti_dupa_autor");
        return cartiByAutor.getOrDefault(autorNume, new ArrayList<>());
    }

    public void imprumutaCarte(String id) throws CarteNegasitaException, CarteNedisponibilaException {
        auditService.logAction("imprumuta_carte");
        Carte carte = cautaCarte(id);
        if (!carte.isDisponibila()) {
            throw new CarteNedisponibilaException("Cartea este indisponibila temporar");
        }
        carte.setDisponibila(false);
        carte.setNumarImprumuturi(carte.getNumarImprumuturi() + 1);
    }

    public void returneazaCarte(String id) throws CarteNegasitaException {
        auditService.logAction("returneaza_carte");
        Carte carte = cautaCarte(id);
        carte.setDisponibila(true);
    }

    public List<Carte> cartiCuCeleMaiMulteImprumuturi() {
        auditService.logAction("carti_cu_cele_mai_multe_imprumuturi");
        List<Carte> result = new ArrayList<>(cartiById.values());
        result.sort(Comparator.comparingInt(Carte::getNumarImprumuturi).reversed());
        return result;
    }

    public boolean esteDisponibila(String id) throws CarteNegasitaException {
        auditService.logAction("verifica_disponibilitate");
        return cautaCarte(id).isDisponibila();
    }
}
