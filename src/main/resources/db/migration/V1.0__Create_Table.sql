-- Table: public.urldata

-- DROP TABLE IF EXISTS public.urldata;

CREATE TABLE IF NOT EXISTS public.urldata
(
    id uuid NOT NULL,
    shorturl character varying(5000) COLLATE pg_catalog."default",
    longurl character varying(5000) COLLATE pg_catalog."default",
    created_at timestamp,
    CONSTRAINT urldata_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;
