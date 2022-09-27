WITH subquery AS (
    SELECT 'Root' || path AS concatenated from public.product_categories
)
UPDATE public.product_categories
SET path = subquery.concatenated
    FROM subquery
WHERE path @> subpath(subquery.concatenated, 1);

INSERT INTO public.product_categories (name, path) VALUES ('Root', 'Root');