CREATE SCHEMA IF NOT EXISTS project;

SET search_path TO project;

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    name            VARCHAR(100) NOT NULL,
    role            VARCHAR(20) NOT NULL
);

CREATE TABLE profile (
    id                      BIGSERIAL PRIMARY KEY,
    user_id                 BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    age                     INTEGER,
    height                  DOUBLE PRECISION,
    weight                  DOUBLE PRECISION,
    gender                  VARCHAR(20),
    activity_level          VARCHAR(30),
    goal                    VARCHAR(30),
    daily_calories_target   INTEGER
);

CREATE TABLE recipe (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    calories        INTEGER,
    protein         DOUBLE PRECISION,
    carbs           DOUBLE PRECISION,
    fat             DOUBLE PRECISION,
    cooking_time    INTEGER,
    difficulty      VARCHAR(20)
);

CREATE TABLE ingredient (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    calories_per_100g   DOUBLE PRECISION
);


CREATE TABLE dietary_tag (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE recipe_ingredient (
    recipe_id       BIGINT NOT NULL REFERENCES recipe (id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient (id) ON DELETE CASCADE,
    PRIMARY KEY (recipe_id, ingredient_id)
);


CREATE TABLE recipe_dietary_tag (
    recipe_id       BIGINT NOT NULL REFERENCES recipe (id) ON DELETE CASCADE,
    dietary_tag_id  BIGINT NOT NULL REFERENCES dietary_tag (id) ON DELETE CASCADE,
    PRIMARY KEY (recipe_id, dietary_tag_id)
);

CREATE TABLE profile_dietary_tag (
    profile_id      BIGINT NOT NULL REFERENCES profile (id) ON DELETE CASCADE,
    dietary_tag_id  BIGINT NOT NULL REFERENCES dietary_tag (id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, dietary_tag_id)
);

CREATE TABLE user_favorite_recipes (
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipe (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE meal_plan (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE meal_plan_entry (
    id              BIGSERIAL PRIMARY KEY,
    meal_plan_id    BIGINT NOT NULL REFERENCES meal_plan (id) ON DELETE CASCADE,
    recipe_id       BIGINT NOT NULL REFERENCES recipe (id),
    meal_date       DATE,
    meal_type       VARCHAR(20)
);

CREATE TABLE feedback (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    rating              INTEGER,
    message             TEXT,
    wants_newsletter    BOOLEAN,
    category            VARCHAR(50)
);

INSERT INTO dietary_tag (name)
VALUES ('VEGAN'),
       ('VEGETARIAN'),
       ('GLUTEN_FREE'),
       ('KETO'),
       ('LACTOSE_FREE');
