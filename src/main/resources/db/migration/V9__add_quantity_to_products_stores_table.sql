ALTER TABLE public.products DROP COLUMN quantity;
ALTER TABLE public.products_stores ADD quantity NUMERIC;