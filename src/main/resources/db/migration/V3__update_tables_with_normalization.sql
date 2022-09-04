ALTER TABLE public.receipt DROP COLUMN category;
DROP TYPE category;

ALTER TABLE public.receipt DROP COLUMN image_url;
ALTER TABLE public.receipt ADD image_id UUID;

ALTER TABLE public.products DROP COLUMN image_url;
ALTER TABLE public.products ADD image_id UUID;

CREATE TYPE store_category AS ENUM ('GROCERY', 'SPECIALTY', 'DEPARTMENT', 'WAREHOUSE', 'DISCOUNT', 'CONVENIENCE', 'RESTAURANT');

CREATE TABLE public."stores"(
    id              UUID            DEFAULT uuid_generate_v4()      NOT NULL,
    name            TEXT                                            NOT NULL,
    category        store_category                                  NOT NULL,
    PRIMARY KEY(id)
);

ALTER TABLE public.receipt ADD store_id UUID;
ALTER TABLE public.receipt ADD CONSTRAINT fk_receipt_stores FOREIGN KEY (store_id) REFERENCES public.stores(id);

CREATE TABLE public."images"(
    id              UUID            DEFAULT uuid_generate_v4()      NOT NULL,
    aws_s3_url      TEXT                                            NOT NULL,
    PRIMARY KEY(id)
);

ALTER TABLE public.receipt ADD CONSTRAINT fk_receipt_images FOREIGN KEY (image_id) REFERENCES public.images(id);
ALTER TABLE public.products ADD CONSTRAINT fk_product_images FOREIGN KEY (image_id) REFERENCES public.images(id);
ALTER TABLE public.products DROP COLUMN expired_at;

CREATE TABLE public."products_stores"(
    product_id      UUID                                                    NOT NULL,
    store_id        UUID                                                    NOT NULL,
    updated_at      TIMESTAMPTZ     DEFAULT timezone('utc'::TEXT, NOW())    NOT NULL,
    expired_at      TIMESTAMPTZ                                             NULL,
    PRIMARY KEY(product_id, store_id)
);
