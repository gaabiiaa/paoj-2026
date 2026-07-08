# Proiect PAO - Biblioteca

## Tema

Aplicatie pentru administrarea unei biblioteci: carti, autori, cititori, sectiuni si imprumuturi.

## Actiuni / interogari posibile

1. Adauga o carte noua in biblioteca.
2. Inregistreaza un cititor nou.
3. Imprumuta o carte unui cititor.
4. Returneaza o carte imprumutata.
5. Cauta carti dupa autor.
6. Listeaza toate cartile ordonate dupa titlu.
7. Afiseaza istoricul imprumuturilor unui cititor.
8. Verifica disponibilitatea unei carti.
9. Afiseaza cartile ordonate dupa numarul de imprumuturi.
10. Sterge un cititor din sistem.
11. Listeaza cartile dintr-o sectiune.

## Tipuri de obiecte din domeniu

- Carte
- Autor
- Cititor
- Persoana
- Angajat
- Bibliotecar
- Sectiune
- Imprumut
- ISBN

## Structura

- `model` - clasele de domeniu si ierarhia OOP.
- `service` - servicii Singleton pentru logica aplicatiei si audit.
- `repository` - repository-uri JDBC cu operatii CRUD.
- `util` - configurarea conexiunii la baza de date.
- `exception` - exceptii custom.

## Persistenta

Configurarea conexiunii se afla in `resources/db.properties`, iar schema bazei de date in `resources/schema.sql`.
Repository-urile folosesc JDBC direct, `PreparedStatement` si `try-with-resources`.
