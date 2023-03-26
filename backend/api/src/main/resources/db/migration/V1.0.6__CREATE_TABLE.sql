CREATE SEQUENCE IF NOT EXISTS pk_sequence_recipe
    start 1
  increment 1;


CREATE TABLE IF NOT EXISTS public.recipe (
    id BIGSERIAL NOT NULL constraint recipe_id_fk primary key,
    content_id varchar(255) not null,
    canonical_url varchar(255) not null,
    slug varchar(255),
    title varchar(255),
    thumbnail varchar(255),
    summary varchar(1024),
    transcript varchar,
    num_ingredients bigint,
    num_steps bigint,
    cooking_time_minutes bigint,
    ingredients varchar,
    instructions varchar,
    processed boolean not null default false,
    failed boolean not null default false,
    reason_failed varchar(255),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(slug),
    UNIQUE(content_id),
    UNIQUE(canonical_url)
    );
ALTER TABLE public.recipe OWNER TO root;