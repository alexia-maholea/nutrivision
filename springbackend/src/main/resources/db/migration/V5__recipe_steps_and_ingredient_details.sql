SET search_path TO project;

ALTER TABLE recipe_ingredient
    ADD COLUMN IF NOT EXISTS quantity DOUBLE PRECISION;

ALTER TABLE recipe_ingredient
    ADD COLUMN IF NOT EXISTS unit VARCHAR(30);

CREATE TABLE IF NOT EXISTS recipe_step (
    id                BIGSERIAL PRIMARY KEY,
    recipe_id         BIGINT NOT NULL REFERENCES recipe (id) ON DELETE CASCADE,
    step_no           INTEGER NOT NULL,
    description       TEXT NOT NULL,
    duration_minutes  INTEGER
);

CREATE UNIQUE INDEX IF NOT EXISTS recipe_step_recipe_step_no_uidx
    ON recipe_step (recipe_id, step_no);

