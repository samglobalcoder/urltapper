
CREATE TABLE IF NOT EXISTS public.urldata
(
    id uuid NOT NULL,
    shorturl character varying(150) COLLATE pg_catalog."default",
    longurl character varying(2000) COLLATE pg_catalog."default",
    created_at timestamp,
    CONSTRAINT urldata_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS urldata_shorturl_idx
    ON urldata(shorturl)