CREATE TABLE public."products"(
    id                  UUID            DEFAULT uuid_generate_v4()              NOT NULL,
    title               TEXT                                                    NOT NULL,
    image_url           TEXT                                                    NULL,
    price               NUMERIC(5, 2)                                           NULL,
    expired_at          TIMESTAMP       DEFAULT timezone('utc'::TEXT, NOW())    NOT NULL,
    created_at          TIMESTAMP       DEFAULT timezone('utc'::TEXT, NOW())    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TYPE category AS ENUM ('GROCERY', 'HOUSEHOLD', 'RESTAURANT', 'MISCELLANEOUS');

CREATE TABLE public."receipt"(
    id              UUID                DEFAULT uuid_generate_v4()              NOT NULL,
    title           TEXT                                                        NOT NULL,
    price           NUMERIC(5, 2)                                               NOT NULL,
    category        category                                                    NOT NULL,
    image_url       TEXT                                                        NOT NULL,
    created_at      TIMESTAMP           DEFAULT timezone('utc'::TEXT, NOW())    NOT NULL,
    PRIMARY KEY (id)
);

