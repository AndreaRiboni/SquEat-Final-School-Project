CREATE TABLE Locale (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(32) NOT NULL,
    Indirizzo VARCHAR(64) NOT NULL,
    Cellulare VARCHAR(13) NOT NULL,
    IDAdmin INT NOT NULL,
    Punteggio FLOAT NOT NULL,
    NumRecensioni INT NOT NULL
);

CREATE TABLE Menù (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    IDLocale INT NOT NULL UNIQUE,
    IDProdotto INT NOT NULL,
    Costo FLOAT NOT NULL
);

CREATE TABLE Prodotto (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(64) NOT NULL,
    Ingredienti VARCHAR(128) NOT NULL
);

CREATE TABLE Utente (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(32) NOT NULL,
    Cognome VARCHAR(32) NOT NULL,
    Cellulare VARCHAR(13) NOT NULL UNIQUE,
    Indirizzo VARCHAR(64) NOT NULL,
    Mail VARCHAR(64) NOT NULL UNIQUE,
    Psw VARCHAR(64) NOT NULL,
    Privilegio INT NOT NULL
);

CREATE TABLE Ordine (
    IDOrdine INT PRIMARY KEY AUTO_INCREMENT,
    IDCliente INT NOT NULL,
    IDLocale INT NOT NULL,
    IDProdotto INT NOT NULL,
    Giorno DATE NOT NULL,
    Costo FLOAT NOT NULL,
    Stato INT NOT NULL,
    IDFattorino INT NOT NULL
);

CREATE TABLE Post(
    IDPost INT PRIMARY KEY AUTO_INCREMENT,
    IDUtente INT NOT NULL,
    IDLocale INT NOT NULL,
    Testo VARCHAR(512) NOT NULL,
    Foto BLOB, 
    Data DATE NOT NULL
);

CREATE TABLE Commenti(
    IDUtente INT NOT NULL,
    IDPost INT NOT NULL,
    Testo VARCHAR(256) NOT NULL,
    PRIMARY KEY(IDUtente, IDPost, Testo)
);

#reazione a commento o a post
CREATE TABLE Interazioni(
    IDUtente INT NOT NULL,
    IDPost INT,
    IDCommento INT,
    Reazione BOOLEAN NOT NULL
);
