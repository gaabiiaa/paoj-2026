package com.pao.proiect.biblioteca;

import com.pao.proiect.biblioteca.exception.CarteNegasitaException;
import com.pao.proiect.biblioteca.exception.CarteNedisponibilaException;
import com.pao.proiect.biblioteca.exception.CititorNegasitException;
import com.pao.proiect.biblioteca.model.*;
import com.pao.proiect.biblioteca.repository.AutorRepository;
import com.pao.proiect.biblioteca.repository.CarteRepository;
import com.pao.proiect.biblioteca.repository.CititorRepository;
import com.pao.proiect.biblioteca.repository.ImprumutRepository;
import com.pao.proiect.biblioteca.service.CarteService;
import com.pao.proiect.biblioteca.service.CititorService;
import com.pao.proiect.biblioteca.service.SectiuneService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--db".equals(args[0])) {
            ruleazaDemoJdbc();
            return;
        }

        CarteService carteService = CarteService.getInstance();
        CititorService cititorService = CititorService.getInstance();
        SectiuneService sectiuneService = SectiuneService.getInstance();

        Autor autor1 = new Autor("A1", "George Orwell", "orwell@email.com", "britanic");
        Autor autor2 = new Autor("A2", "J.R.R. Tolkien", "tolkien@email.com", "britanic");
        ISBN isbn = new ISBN("978-973-46-0000-1");

        Carte carte1 = new Carte("C1", "1984", autor1, "Fictiune", true);
        Carte carte2 = new Carte("C2", "Hobit", autor2, "Fantasy", true);
        Carte carte3 = new Carte("C3", "Animal Farm", autor1, "Satira", true);

        carteService.adaugaCarte(carte1);
        carteService.adaugaCarte(carte2);
        carteService.adaugaCarte(carte3);

        Sectiune sectiune = new Sectiune("S1", "Fictiune");
        sectiune.adaugaCarte(carte1);
        sectiune.adaugaCarte(carte2);
        sectiuneService.adaugaSectiune(sectiune);

        Cititor cititor = new Cititor("R1", "Ana Popescu", "ana@email.com");
        cititorService.adaugaCititor(cititor);

        System.out.println("ISBN imutabil demonstrativ: " + isbn);
        System.out.println("1. Adauga carte noua: " + carte1);
        System.out.println("2. Inregistreaza cititor nou: " + cititor);

        try {
            carteService.imprumutaCarte("C1");
            Imprumut imprumut = new Imprumut("I1", carte1, cititor, LocalDate.now());
            cititorService.adaugaImprumut("R1", imprumut);
            System.out.println("3. Imprumuta carte: " + imprumut);

            carteService.returneazaCarte("C1");
            imprumut.setDataReturnarii(LocalDate.now());
            System.out.println("4. Returneaza carte: " + imprumut);
        } catch (CarteNegasitaException | CarteNedisponibilaException | CititorNegasitException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        try {
            System.out.println("5. Cauta carti dupa autor: " + carteService.cautaDupaAutor("George Orwell"));
            System.out.println("6. Listeaza toate cartile: " + carteService.toateCartile());
            System.out.println("7. Istoric imprumuturi: " + cititorService.istoriculImprumuturilor("R1"));
            System.out.println("8. Verifica disponibilitate C1: " + carteService.esteDisponibila("C1"));
            System.out.println("9. Carti cu cele mai multe imprumuturi: " + carteService.cartiCuCeleMaiMulteImprumuturi());
            System.out.println("10. Sterge cititor:");
            cititorService.stergeCititor("R1");
            System.out.println("Cititorul a fost sters.");
        } catch (CarteNegasitaException | CititorNegasitException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("11. Listeaza carti din sectiune: " + sectiuneService.cartiDinSectiune("S1"));
        System.out.println("Auditul actiunilor a fost scris in audit.csv.");
    }

    private static void ruleazaDemoJdbc() {
        AutorRepository autorRepository = new AutorRepository();
        CarteRepository carteRepository = new CarteRepository();
        CititorRepository cititorRepository = new CititorRepository();
        ImprumutRepository imprumutRepository = new ImprumutRepository();

        Autor autor = new Autor("DB-A1", "Mary Shelley", "mary@shelley.test", "britanica");
        Carte carte = new Carte("DB-C1", "Frankenstein", autor, "Gotic", true);
        Cititor cititor = new Cititor("DB-R1", "Mihai Ionescu", "mihai@test.ro");
        Imprumut imprumut = new Imprumut("DB-I1", carte, cititor, LocalDate.now());

        autorRepository.save(autor);
        carteRepository.save(carte);
        cititorRepository.save(cititor);
        imprumutRepository.imprumutaCarteTransactional(imprumut);

        System.out.println("Carti cu autori: " + carteRepository.findCartiCuAutori());
        System.out.println("Imprumuturi active: " + imprumutRepository.findImprumuturiActiveCuCititoriSiCarti());
        System.out.println("Numar imprumuturi per cititor: " + imprumutRepository.findNumarImprumuturiPerCititor());
    }
}
