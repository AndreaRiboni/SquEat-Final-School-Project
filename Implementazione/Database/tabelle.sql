CREATE TABLE Locale (
    ID INT PRIMARY KEY,
    Nome VARCHAR(32) NOT NULL,
    Indirizzo VARCHAR(64) NOT NULL,
    Cellulare VARCHAR(13) NOT NULL,
    IDAdmin INT NOT NULL UNIQUE
);

CREATE TABLE Menù (
    ID INT PRIMARY KEY,
    IDLocale INT NOT NULL UNIQUE,
    IDProdotto INT NOT NULL UNIQUE,
    Costo FLOAT NOT NULL
);

CREATE TABLE Prodotto (
    ID INT PRIMARY KEY,
    Nome VARCHAR(64) NOT NULL,
    Ingredienti VARCHAR(128) NOT NULL
);

CREATE TABLE Utente (
    ID INT PRIMARY KEY,
    Nome VARCHAR(32) NOT NULL,
    Cognome VARCHAR(32) NOT NULL,
    Cellulare VARCHAR(13) NOT NULL UNIQUE,
    Indirizzo VARCHAR(64) NOT NULL,
    Mail VARCHAR(64) NOT NULL UNIQUE,
    Psw VARCHAR(64) NOT NULL,
    Privilegio INT NOT NULL
);

CREATE TABLE Ordine (
    IDOrdine INT PRIMARY KEY,
    IDCliente INT NOT NULL,
    IDLocale INT NOT NULL,
    IDProdotto INT NOT NULL,
    Giorno DATE NOT NULL,
    Costo FLOAT NOT NULL,
    Stato INT NOT NULL,
    IDFattorino INT NOT NULL
);

