DROP TABLE IF EXISTS imprumuturi;
DROP TABLE IF EXISTS sectiune_carti;
DROP TABLE IF EXISTS carti;
DROP TABLE IF EXISTS sectiuni;
DROP TABLE IF EXISTS cititori;
DROP TABLE IF EXISTS autori;

CREATE TABLE autori (
    id VARCHAR(50) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    nationalitate VARCHAR(100) NOT NULL
);

CREATE TABLE cititori (
    id VARCHAR(50) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    activ BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE sectiuni (
    id VARCHAR(50) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL
);

CREATE TABLE carti (
    id VARCHAR(50) PRIMARY KEY,
    titlu VARCHAR(150) NOT NULL,
    autor_id VARCHAR(50) NOT NULL,
    categorie VARCHAR(100) NOT NULL,
    disponibila BOOLEAN NOT NULL DEFAULT TRUE,
    numar_imprumuturi INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_carti_autori FOREIGN KEY (autor_id) REFERENCES autori(id)
);

CREATE TABLE sectiune_carti (
    sectiune_id VARCHAR(50) NOT NULL,
    carte_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (sectiune_id, carte_id),
    CONSTRAINT fk_sectiune_carti_sectiuni FOREIGN KEY (sectiune_id) REFERENCES sectiuni(id),
    CONSTRAINT fk_sectiune_carti_carti FOREIGN KEY (carte_id) REFERENCES carti(id)
);

CREATE TABLE imprumuturi (
    id VARCHAR(50) PRIMARY KEY,
    carte_id VARCHAR(50) NOT NULL,
    cititor_id VARCHAR(50) NOT NULL,
    data_imprumutului DATE NOT NULL,
    data_returnarii DATE,
    CONSTRAINT fk_imprumuturi_carti FOREIGN KEY (carte_id) REFERENCES carti(id),
    CONSTRAINT fk_imprumuturi_cititori FOREIGN KEY (cititor_id) REFERENCES cititori(id)
);
