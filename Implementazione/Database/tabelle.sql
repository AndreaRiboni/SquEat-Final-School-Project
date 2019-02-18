CREATE TABLE Locale (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(32) NOT NULL,
    Indirizzo VARCHAR(64) NOT NULL,
    Cellulare VARCHAR(13) NOT NULL,
    IDAdmin INT NOT NULL,
    Punteggio FLOAT NOT NULL,
    NumRecensioni INT NOT NULL
);

CREATE TABLE Prodotto (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(64) NOT NULL,
    Ingredienti VARCHAR(128) NOT NULL
);

CREATE TABLE MenuLoc (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    IDLocale INT NOT NULL,
    IDProdotto INT NOT NULL,
    Costo FLOAT NOT NULL,
    FOREIGN KEY (IDLocale) REFERENCES Locale(ID)
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
    Timestamp VARCHAR(16) NOT NULL,
    Costo FLOAT NOT NULL,
    Stato INT NOT NULL,
    IDFattorino INT NOT NULL,
    FOREIGN KEY (IDLocale) REFERENCES Locale(ID),
    FOREIGN KEY (IDCliente) REFERENCES Utente(ID),
    FOREIGN KEY (IDFattorino) REFERENCES Utente(ID),
    FOREIGN KEY (IDProdotto) REFERENCES Prodotto(ID)
);

CREATE TABLE Post(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    IDUtente INT NOT NULL,
    IDLocale INT NOT NULL,
    Testo VARCHAR(512) NOT NULL,
    Foto BLOB, 
    Data DATE NOT NULL,
    FOREIGN KEY (IDUtente) REFERENCES Utente(ID),
    FOREIGN KEY (IDLocale) REFERENCES Locale(ID)
);

CREATE TABLE Commenti(
	ID INT PRIMARY KEY AUTO_INCREMENT,
    IDUtente INT NOT NULL,
    IDPost INT NOT NULL,
    Testo VARCHAR(256) NOT NULL,
    FOREIGN KEY (IDUtente) REFERENCES Utente(ID),
    FOREIGN KEY (IDPost) REFERENCES Post(ID)
);

#reazione a commento o a post
CREATE TABLE Interazioni(
    IDUtente INT NOT NULL,
    IDPost INT,
    IDCommento INT,
    Reazione BOOLEAN NOT NULL,
    FOREIGN KEY (IDUtente) REFERENCES Utente(ID),
    FOREIGN KEY (IDPost) REFERENCES Post(ID),
    FOREIGN KEY (IDCommento) REFERENCES Commenti(ID)
);

CREATE TABLE fattorini (
	IDLocale int,
    IDCliente int,
    primary key(IDLocale, IDCliente),
    FOREIGN KEY (IDLocale) REFERENCES Locale(ID),
    FOREIGN KEY (IDCliente) REFERENCES Utente(ID)
);

INSERT INTO Locale VALUES (
    1,
    "Da Totto",
    "45.46, 9.19",
    "3663574756",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    2,
    "Da Ribo",
    "45.53, 9.40",
    "3453797411",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    3,
    "Da Pietro",
    "45.53, 9.23",
    "3473560285",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    4,
    "Da Sapo",
    "45.50, 9.22",
    "3333165852",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    5,
    "Da Vise",
    "45.51, 9.22",
    "3935552210",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    6,
    "Da Duccio",
    "45.69, 9.14",
    "3839255903",
    1,
    0,
    0
);

INSERT INTO Locale VALUES (
    7,
    "Bella Napoli",
    "40.85, 14.26",
    "081426300",
    1,
    0,
    0
);

INSERT INTO Utente VALUES (
    1,
    "admin",
    "admin",
    "3453797411",
    "53.22, -4.20",
    "admin@admin.it",
    "admin",
    0
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Margherita",
    "Pomodoro, Mozzarella"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza con Porcini",
    "Pomodoro, Mozzarella, Funghi"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Siciliana",
    "Pomodoro, Fior di Latte, Melanzane, Cotto"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Tonno e Cipolla",
    "Pomodoro, Mozzarella, Tonno, Cipolla"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Diavola",
    "Pomodoro, Mozzarella, Salame Piccante"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Ortolana",
    "Pomodoro, Mozzarella, Peperoni, Melanzane, Funghi"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Sfiziosa",
    "Pomodoro, Mozzarella, Porcini, Patate"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Marinara",
    "Aglio, Pomodoro, Origano"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza 4 Stagioni",
    "Pomodoro, Mozzarella, Cotto, Funghi, Salame, Melanzane"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Capricciosa",
    "Pomodoro, Mozzarella, Cotto, Funghi, Salame, Olive"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza 4 Formaggi",
    "Panna, Mozzarella, Gorgonzola, Emmenthal, Provolone"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Americana",
    "Pomodoro, Mozzarella, Patatine, Wurstel"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Romana",
    "Pomodoro, Mozzarella, Acciughe"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Pizza Napoli",
    "Pomodoro, Aglio, Alici, Acciughe"
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    2,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    3,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    4,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    5,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    6,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    7,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    8,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    9,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    10,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    11,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    12,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    13,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    14,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    2,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    3,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    4,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    5,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    6,
    1,
    4
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    7,
    1,
    4
);

INSERT INTO Utente values (
    2,
    "fatto",
    "rino",
    "3348075810",
    "44.45, 9.19",
    "fatto@ri.no",
    "fattorino",
    0
);

INSERT INTO Fattorini VALUES (
    1, 2
);

INSERT INTO Fattorini VALUES (
    2, 2
);

INSERT INTO Fattorini VALUES (
    3, 2
);

INSERT INTO Fattorini VALUES (
    4, 2
);

INSERT INTO Fattorini VALUES (
    5, 2
);

INSERT INTO Fattorini VALUES (
    6, 2
);

INSERT INTO Fattorini VALUES (
    7, 2
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Acqua Naturale",
    "Acqua Naturale"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Acqua Frizzante",
    "Acqua Frizzante"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Coca Cola",
    "Coca Cola"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Sprite",
    "Sprite"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Fanta",
    "Fanta"
);

INSERT INTO Prodotto (Nome, Ingredienti) VALUES (
    "Birra",
    "Birra"
);


INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    15,
    1
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    16,
    1
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    17,
    2
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    18,
    2
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    19,
    2
);

INSERT INTO MenuLoc (IDLocale, IDProdotto, Costo) VALUES (
    1,
    20,
    3
);

CREATE TABLE Telegram (
    IDChat INT PRIMARY KEY,
    IDUtente INT,
    Stato INT,
    Nome varchar(32),
    Cognome varchar(32),
    Cellulare varchar(32),
    Indirizzo varchar(64),
    Mail varchar(64),
    Extra varchar(64),
    CONSTRAINT FOREIGN KEY (IDUtente) REFERENCES Utente(ID)
);