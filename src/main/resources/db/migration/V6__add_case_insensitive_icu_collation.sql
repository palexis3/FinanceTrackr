CREATE COLLATION english_ci (
    PROVIDER = 'icu',
    LOCALE = 'en-US@colStrength=secondary',
    DETERMINISTIC = FALSE
);

ALTER TABLE public.stores ALTER name TYPE TEXT COLLATE english_ci;
ALTER TABLE public.products RENAME COLUMN title TO name;
ALTER TABLE public.products ALTER name TYPE TEXT COLLATE english_ci;