ALTER TABLE public.recipe
    ADD COLUMN description varchar(255),
    ADD COLUMN keywords varchar(255),
    ADD COLUMN published boolean default false;
