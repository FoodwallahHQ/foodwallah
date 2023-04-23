ALTER TABLE public.recipe
    ADD COLUMN additional_info varchar,
    DROP COLUMN cooking_time_minutes;
