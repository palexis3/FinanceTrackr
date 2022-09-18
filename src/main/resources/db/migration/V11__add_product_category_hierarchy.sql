CREATE EXTENSION IF NOT EXISTS ltree;

CREATE TABLE public.product_categories(
    name            TEXT            NOT NULL,
    path            ltree           UNIQUE NOT NULL,
    PRIMARY KEY (name)
);

CREATE INDEX path_idx ON public.product_categories USING btree(path);


INSERT INTO public.product_categories (name, path)
    VALUES ('Beauty', 'Beauty'),
           ('Bath & Body', 'Beauty.Bath_Body'),
           ('Fragrances', 'Beauty.Fragrances'),
           ('Skin Care', 'Beauty.Skin_Care'),
           ('Sunscreen', 'Beauty.Sunscreen'),
           ('Clothing', 'Clothing'),
           ('Electronics', 'Electronics'),
           ('Grocery', 'Grocery'),
           ('Beverages', 'Grocery.Beverages'),
           ('Pantry', 'Grocery.Pantry'),
           ('Seafood', 'Grocery.Seafood'),
           ('Poultry', 'Grocery.Poultry'),
           ('Dairy', 'Grocery.Dairy'),
           ('Cold & Frozen', 'Grocery.Cold_Frozen'),
           ('Household Essentials', 'Household_Essentials'),
           ('Cleaning Supplies', 'Household_Essentials.Cleaning_Supplies'),
           ('Laundry', 'Household_Essentials.Laundry'),
           ('Paper & Plastics', 'Household_Essentials.Paper_Plastics'),
           ('Health & Personal Care', 'Health_Personal_Care'),
           ('Nutrition', 'Health_Personal_Care.Nutrition'),
           ('Protein Shake', 'Health_Personal_Care.Nutrition.Protein_Shake'),
           ('Personal Care', 'Personal_Care'),
           ('Cleansing Wipes', 'Personal_Care.Cleansing_Wipes'),
           ('Deodorant', 'Personal_Care.Deodorant'),
           ('Hair Care', 'Personal_Care.Hair_Care'),
           ('Moisturizer', 'Personal_Care.Moisturizer'),
           ('Oral Care', 'Personal_Care.Oral_Care'),
           ('Shaving', 'Personal_Care.Shaving'),
           ('Soap', 'Personal_Care.Soap');

ALTER TABLE public.products ADD product_category TEXT;
ALTER TABLE public.products
    ADD CONSTRAINT fk_product_category_name FOREIGN KEY (product_category)
    REFERENCES public.product_categories(name);



