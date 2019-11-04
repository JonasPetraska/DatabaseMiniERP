﻿-- Dalykines taisykles, uztikrinancios pirkimo uzsakymo busenu vientisuma Sukurtas -> Apmoketas -> Pristatytas

CREATE FUNCTION sukurtasUzsakymas() RETURNS "trigger" as $$
DECLARE apmoketu   SMALLINT;
DECLARE pristatytu SMALLINT;
	BEGIN
	
		/* Patikrinam ar apmoketas */
		SELECT COUNT(*) INTO apmoketu FROM jope4327.PirkimoUzsakymasApmoketas
			WHERE Pirkimo_Uzsakymas = NEW.Pirkimo_Uzsakymas;
		
		/* Patikrinam ar pristatytas */
		SELECT COUNT(*) INTO pristatytu FROM jope4327.PirkimoUzsakymasPristatytas
			WHERE Pirkimo_Uzsakymas = NEW.Pirkimo_Uzsakymas;
			
		IF apmoketu > 0 
			THEN RAISE EXCEPTION 'Uzsakymas jau yra apmoketas';
		END IF;
		
		IF pristatytu > 0
			THEN RAISE EXCEPTION 'Uzsakymas jau yra pristatytas';
		END IF;
		RETURN NEW;
	END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER sukurtamUzsakymui
	BEFORE INSERT ON jope4327.PirkimoUzsakymasSukurtas
	FOR EACH ROW EXECUTE PROCEDURE sukurtasUzsakymas();

	
CREATE FUNCTION apmoketasUzsakymas() RETURNS "trigger" as $$
DECLARE pristatytu SMALLINT;
DECLARE uzsakymo_eiluciu INTEGER;
	BEGIN
		
		/* Patikrinam ar pristatytas */
		SELECT COUNT(*) INTO pristatytu FROM jope4327.PirkimoUzsakymasPristatytas
			WHERE Pirkimo_Uzsakymas = NEW.Pirkimo_Uzsakymas;
			
		/* Patikrinam ar turi uzsakymo eiluciu */
		SELECT COUNT(*) INTO uzsakymo_eiluciu FROM jope4327.PirkimoUzsakymoEilute
			WHERE Uzsakymas = NEW.Pirkimo_Uzsakymas;
		
		IF pristatytu > 0
			THEN RAISE EXCEPTION 'Uzsakymas jau yra pristatytas';
		END IF;
		
		IF uzsakymo_eiluciu = 0
			THEN RAISE EXCEPTION 'Pridekite uzsakymo eiluciu';
		END IF;
		
		RETURN NEW;
	END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER apmoketamUzsakymui
	BEFORE INSERT ON jope4327.PirkimoUzsakymasApmoketas
	FOR EACH ROW EXECUTE PROCEDURE apmoketasUzsakymas();


CREATE FUNCTION pristatytasUzsakymas() RETURNS "trigger" as $$
DECLARE apmoketu SMALLINT;
	BEGIN
		
		/* Patikrinam ar apmoketas */
		SELECT COUNT(*) INTO apmoketu FROM jope4327.PirkimoUzsakymasApmoketas
			WHERE Pirkimo_Uzsakymas = NEW.Pirkimo_Uzsakymas
		
		IF apmoketu > 0
			THEN RAISE EXCEPTION 'Uzsakymas nera apmoketas';
		END IF;
		
		
		RETURN NEW;
	END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER pristatytamUzsakymui
	BEFORE INSERT ON jope4327.PirkimoUzsakymasPristatytas
	FOR EACH ROW EXECUTE PROCEDURE pristatytasUzsakymas();