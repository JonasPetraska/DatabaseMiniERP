CREATE TABLE jope4327.Klientas
(
    Nr                 SERIAL        NOT NULL      CHECK (Nr > 0),		
    ElPasto_Adresas    VARCHAR(50)   NOT NULL      CONSTRAINT TinkamasElPastoAdresas
						      CHECK(ElPasto_Adresas ~ '^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$'),
    Tel_Nr	       	   CHAR(12),
    
    PRIMARY KEY (Nr)	

);

CREATE TABLE jope4327.JuridinisKlientas
(
    Imones_Nr          CHAR(9)        NOT NULL      CONSTRAINT TinkamasImonesNr
						       CHECK ((char_length(Imones_Nr) >= 7) AND (char_length(Imones_Nr) <= 9)),
    Imones_Pavadinimas VARCHAR(30)    NOT NULL,
    Klientas           SMALLINT       NOT NULL,

    PRIMARY KEY (Imones_Nr),
    FOREIGN KEY (Klientas) REFERENCES Klientas
                           ON DELETE CASCADE
);

CREATE TABLE jope4327.FizinisKlientas
(
    Asmens_Kodas       CHAR(11)        NOT NULL      CONSTRAINT TinkamasAsmensKodas    CHECK (char_length(Asmens_Kodas) = 11),
    Vardas             VARCHAR(15)     NOT NULL,
    Pavarde            VARCHAR(20)     NOT NULL,
    Klientas           SMALLINT        NOT NULL,

    PRIMARY KEY (Asmens_Kodas),
    FOREIGN KEY (Klientas) REFERENCES Klientas
                           ON DELETE CASCADE
);

CREATE TABLE jope4327.Adresas
(
    Salis              VARCHAR(20)     NOT NULL,
    Miestas	           VARCHAR(15)     NOT NULL,
    Buto_Nr            INTEGER         NOT NULL,
    Gatve              VARCHAR(20)     NOT NULL,
    Namo_Nr            INTEGER         NOT NULL,
    Klientas           SMALLINT        NOT NULL,

    PRIMARY KEY (Salis, Miestas, Gatve, Namo_Nr),
    FOREIGN KEY (Klientas) REFERENCES Klientas
                           ON DELETE CASCADE
);

CREATE TABLE jope4327.SaskaitaFaktura
(
    Nr		           SERIAL          NOT NULL,
    Issiuntimo_Data    DATE            NOT NULL,
    Apmokejimo_Data    DATE            NOT NULL,
    Klientas           SMALLINT        NOT NULL,

    PRIMARY KEY (Nr),
    FOREIGN KEY (Klientas) REFERENCES Klientas
                           ON DELETE SET NULL
);


CREATE TABLE jope4327.PirkimoUzsakymas
(
    Nr		           SERIAL          NOT NULL,
    Klientas           SMALLINT        NOT NULL,

    PRIMARY KEY (Nr),
    FOREIGN KEY (Klientas) REFERENCES Klientas
                           ON DELETE SET NULL
);

CREATE TABLE jope4327.PirkimoUzsakymasSukurtas
(
    Data   	       	   DATE            DEFAULT CURRENT_DATE,		
    Pirkimo_Uzsakymas  SMALLINT        NOT NULL,

    PRIMARY KEY (Data, Pirkimo_Uzsakymas),
    FOREIGN KEY (Pirkimo_Uzsakymas) REFERENCES PirkimoUzsakymas
                                    ON DELETE RESTRICT
);

CREATE TABLE jope4327.Vairuotojas
(
    Asmens_Kodas       CHAR(11)        NOT NULL      CONSTRAINT TinkamasAsmensKodas
							CHECK (char_length(Asmens_Kodas) = 11),
    Vardas             VARCHAR(15)     NOT NULL,
    Pavarde            VARCHAR(20)     NOT NULL,

    PRIMARY KEY (Asmens_Kodas)
);

CREATE TABLE jope4327.PirkimoUzsakymasApmoketas
(
    Saskaita_Faktura   SMALLINT        NOT NULL,
    Pirkimo_Uzsakymas  SMALLINT        NOT NULL,

    PRIMARY KEY (Saskaita_Faktura, Pirkimo_Uzsakymas),
    FOREIGN KEY (Saskaita_Faktura)  REFERENCES SaskaitaFaktura
									ON DELETE RESTRICT,
    FOREIGN KEY (Pirkimo_Uzsakymas) REFERENCES PirkimoUzsakymas
                                    ON DELETE RESTRICT
);

CREATE TABLE jope4327.PirkimoUzsakymasPristatytas
(
    Nr                 SERIAL          NOT NULL,
    Pirkimo_Uzsakymas  SMALLINT        NOT NULL,
    Vairuotojas        CHAR(11)	       NOT NULL,

    PRIMARY KEY (Nr),
    FOREIGN KEY (Pirkimo_Uzsakymas)  REFERENCES PirkimoUzsakymas
                                    ON DELETE RESTRICT,
    FOREIGN KEY (Vairuotojas)       REFERENCES Vairuotojas
                                    ON DELETE RESTRICT
);

CREATE TABLE jope4327.Preke
(
    Nr                 SERIAL          NOT NULL,
    Pavadinimas        VARCHAR(20)     NOT NULL,
    Tipas              VARCHAR(15)     CHECK (Tipas IN('Maisto', 'Kosmetikos', 'Statybine', 'Elektronine', 'Higienos', 'Nenustatyta')) 	DEFAULT 'Nenustatyta',
    Aprasymas          VARCHAR(50),
    Kaina              SMALLINT        NOT NULL,     CHECK (Kaina > 0),

    PRIMARY KEY (Nr)
);


CREATE TABLE jope4327.PirkimoUzsakymoEilute
(
    Preke              SMALLINT        NOT NULL,
    Uzsakymas          SMALLINT        NOT NULL,
    Kiekis             INTEGER         NOT NULL      CHECK (Kiekis > 0),

    PRIMARY KEY (Preke, Uzsakymas),
    FOREIGN KEY (Uzsakymas) REFERENCES PirkimoUzsakymas
                            ON DELETE RESTRICT,
    FOREIGN KEY (Preke)     REFERENCES Preke
                            ON DELETE RESTRICT
);

