CREATE OR REPLACE FUNCTION public.cuentaArtistas()
RETURNS int
	LANGUAGE plpgsql
AS $$
declare
contador integer;
BEGIN
    select count(*) into contador from artistas;
return contador;
END;
$$;