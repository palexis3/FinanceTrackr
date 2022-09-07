ALTER TABLE public.stores ADD quantity NUMERIC;
ALTER TABLE public.receipts ALTER price SET DATA TYPE NUMERIC(10, 2);
ALTER TABLE public.products ALTER price SET DATA TYPE NUMERIC(10, 2);