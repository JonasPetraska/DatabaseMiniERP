CREATE MATERIALIZED VIEW jope4327.VairuotojuUzsakymai AS
	SELECT A.Asmens_Kodas, B.Nr AS "Uzsakymo Nr.", C.Nr AS "Kliento Nr."
	FROM jope4327.Vairuotojas A, jope4327.PirkimoUzsakymas B, jope4327.Klientas C, jope4327.PirkimoUzsakymasPristatytas D
	WHERE D.Pirkimo_Uzsakymas = B.Nr AND D.Vairuotojas = A.Asmens_Kodas
		AND B.Klientas = C.Nr
	WITH NO DATA;
	
CREATE MATERIALIZED VIEW jope4327.VisiPirkimoUzsakymai AS
	WITH BendraPirkimoUzsakymoKaina(Pirkimo_Uzsakymo_Nr, BendraKaina, EiluciuSkaicius) AS(
		SELECT A.Nr, SUM(C.Kaina*B.Kiekis), COUNT(B)
		FROM jope4327.PirkimoUzsakymas AS A, jope4327.PirkimoUzsakymoEilute AS B, jope4327.Preke AS C 
		WHERE B.Uzsakymas = A.Nr 
			  AND B.Preke = C.Nr
		GROUP BY 1
	)
	SELECT COALESCE(B.Pirkimo_Uzsakymas, C.Pirkimo_Uzsakymas, D.Pirkimo_Uzsakymas) AS "Nr.", 
		   A.Klientas AS "Kliento Nr.", 
		   B.Data AS "Sukurimo data",
		   CASE WHEN C.Saskaita_Faktura IS NOT NULL THEN C.Saskaita_Faktura
				ELSE 0 END AS "Saskaitos fakturos Nr.",
		   COALESCE(D.Vairuotojas, 'Nenustatyta') AS "Pristatancio vairuotojo Nr.",
		   COALESCE(E.BendraKaina, 0) AS "Visa kaina",
		   COALESCE(E.EiluciuSkaicius, 0) AS "Pirkimo eiluciu skaicius", 
		   CASE WHEN D.Pirkimo_Uzsakymas IS NOT NULL THEN 'Pristatytas'
			    WHEN C.Pirkimo_Uzsakymas IS NOT NULL THEN 'Apmoketas'
                ELSE 'Sukurtas' END AS "Busena"
	FROM jope4327.PirkimoUzsakymas AS A
	LEFT JOIN jope4327.PirkimoUzsakymasSukurtas AS B ON B.Pirkimo_Uzsakymas = A.Nr
	LEFT JOIN jope4327.PirkimoUzsakymasApmoketas AS C ON C.Pirkimo_Uzsakymas = A.Nr
	LEFT JOIN jope4327.PirkimoUzsakymasPristatytas AS D ON D.Pirkimo_Uzsakymas = A.Nr
	LEFT JOIN BendraPirkimoUzsakymoKaina AS E ON E.Pirkimo_Uzsakymo_Nr = A.Nr
	ORDER BY 1
	WITH NO DATA;