/*﻿CREATE VIEW jope4327.JuridiniaiKlientai AS
	SELECT Imones_Nr AS "Imones Nr.", Imones_Pavadinimas AS "Imones pavadinimas", ElPasto_Adresas AS "El. Pasto adresas", Tel_Nr AS "Tel. numeris"
	FROM jope4327.Klientas AS A, jope4327.JuridinisKlientas AS B
	WHERE B.Klientas = A.Nr;
	
CREATE VIEW jope4327.FiziniaiKlientai AS
	SELECT Asmens_Kodas AS "Asmens kodas", Vardas, Pavarde, ElPasto_Adresas AS "El. Pasto adresas", Tel_Nr AS "Tel. numeris"
	FROM jope4327.Klientas AS A, jope4327.FizinisKlientas AS B
	WHERE B.Klientas = A.Nr;*/

CREATE VIEW jope4327.VairuotojuPristatomiUzsakymai AS
	SELECT A.Asmens_Kodas AS "Asmens kodas", B.Nr AS "Uzsakymo Nr.", C.Nr AS "Kliento Nr."
	FROM jope4327.Vairuotojas AS A, jope4327.PirkimoUzsakymas AS B, jope4327.Klientas AS C, jope4327.PirkimoUzsakymasPristatytas AS D
	WHERE D.Pirkimo_Uzsakymas = B.Nr AND D.Vairuotojas = A.Asmens_Kodas
		AND B.Klientas = C.Nr;


/*CREATE VIEW jope4327.VisiKlientai AS 
	SELECT Nr AS "Nr.", 
		   Asmens_Kodas AS "Asmens kodas/Imones Nr.",
		   Vardas || ' ' || Pavarde AS "Vardas Pavarde/Imones pavadinimas", 
		   Tel_Nr AS "Telefono numeris", 
		   ElPasto_Adresas AS "El.Pasto Adresas",
		   'F' AS "tipas"
	FROM jope4327.FizinisKlientas, jope4327.Klientas
	WHERE Klientas = Nr
	UNION
	SELECT Nr, 
		   Imones_Nr,
           Imones_Pavadinimas, 
           Tel_Nr, 
		   ElPasto_Adresas,
		   'J'
	FROM jope4327.JuridinisKlientas, jope4327.Klientas
	WHERE Klientas = Nr
	ORDER BY 1;
*/

CREATE VIEW jope4327.VisiKlientai AS 
	SELECT B.Nr AS "Nr.", 
		   B.Tel_Nr AS "Telefono numeris", 
		   B.ElPasto_Adresas AS "El.Pasto Adresas",
		   COALESCE(A.Asmens_Kodas, 'Nenustatyta') AS "Asmens kodas",
		   COALESCE(A.Vardas || ' ' || A.Pavarde, 'Nenustatyta') AS "Vardas Pavarde",
		   COALESCE(C.Imones_Nr, 'Nenustatyta') AS "Imones Nr.",
           COALESCE(C.Imones_Pavadinimas, 'Nenustatyta') AS "Imones pavadinimas",
		   CASE WHEN A.Asmens_Kodas IS NULL THEN 'Juridinis' ELSE 'Fizinis' END AS "Tipas"
	FROM jope4327.Klientas B
	LEFT JOIN jope4327.JuridinisKlientas AS C ON C.Klientas = B.Nr
	LEFT JOIN jope4327.FizinisKlientas AS A ON A.Klientas = B.Nr
	ORDER BY 1;