package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.exception.CititorNegasitException;
import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.model.Imprumut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CititorService {
    private static CititorService instance;
    private final Map<String, Cititor> cititoriById = new HashMap<>();
    private final AuditService auditService = AuditService.getInstance();

    private CititorService() {
    }

    public static CititorService getInstance() {
        if (instance == null) {
            instance = new CititorService();
        }
        return instance;
    }

    public void adaugaCititor(Cititor cititor) {
        if (cititor == null) {
            throw new IllegalArgumentException("Cititor invalid");
        }
        auditService.logAction("adauga_cititor");
        cititoriById.put(cititor.getId(), cititor);
    }

    public Cititor cautaCititor(String id) throws CititorNegasitException {
        auditService.logAction("cauta_cititor");
        Cititor cititor = cititoriById.get(id);
        if (cititor == null) {
            throw new CititorNegasitException("Cititorul cu id " + id + " nu a fost gasit");
        }
        return cititor;
    }

    public void stergeCititor(String id) throws CititorNegasitException {
        auditService.logAction("sterge_cititor");
        Cititor cititor = cautaCititor(id);
        cititoriById.remove(id);
    }

    public List<Cititor> toateCititorii() {
        auditService.logAction("listeaza_cititori");
        return new ArrayList<>(cititoriById.values());
    }

    public void adaugaImprumut(String cititorId, Imprumut imprumut) throws CititorNegasitException {
        auditService.logAction("adauga_imprumut_la_cititor");
        Cititor cititor = cautaCititor(cititorId);
        cititor.adaugaImprumut(imprumut);
    }

    public List<Imprumut> istoriculImprumuturilor(String cititorId) throws CititorNegasitException {
        auditService.logAction("istoric_imprumuturi_cititor");
        return cautaCititor(cititorId).getIstoriculImprumuturilor();
    }
}
