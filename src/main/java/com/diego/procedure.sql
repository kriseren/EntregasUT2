CREATE OR REPLACE PROCEDURE public.borraArtista(n varchar)
	LANGUAGE plpgsql
AS $procedure$
BEGIN
    DELETE FROM artistas WHERE nombre = n;
END;
$procedure$
;