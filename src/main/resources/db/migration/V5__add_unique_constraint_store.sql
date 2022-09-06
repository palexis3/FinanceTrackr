ALTER TABLE public.stores
ADD CONSTRAINT unique_store_name
UNIQUE(name);