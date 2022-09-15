INSERT INTO public.product_categories (name, path)
VALUES ('Fruits', 'Grocery.Fruits'), ('Vegetables', 'Grocery.Vegetables');
ALTER TABLE public.products ALTER COLUMN product_category SET NOT NULL;