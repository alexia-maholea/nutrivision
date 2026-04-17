CREATE SCHEMA IF NOT EXISTS project;

SET search_path TO project;

-- =======================
-- USERS
-- =======================
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL
);

-- =======================
-- PROFILE (1-1 cu users)
-- =======================
CREATE TABLE profile (
                         id SERIAL PRIMARY KEY,
                         user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                         age INTEGER,
                         height DOUBLE PRECISION,
                         weight DOUBLE PRECISION,
                         gender VARCHAR(10),
                         activity_level VARCHAR(20),
                         goal VARCHAR(30),
                         daily_calories_target INTEGER
);

-- =======================
-- RECIPE
-- =======================
CREATE TABLE recipe (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        description TEXT,
                        calories INTEGER,
                        protein DOUBLE PRECISION,
                        carbs DOUBLE PRECISION,
                        fat DOUBLE PRECISION,
                        cooking_time INTEGER,
                        difficulty VARCHAR(20)
);

-- =======================
-- INGREDIENT
-- =======================
CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            calories_per_100g DOUBLE PRECISION
);

-- =======================
-- RECIPE_INGREDIENT (M-M)
-- =======================
CREATE TABLE recipe_ingredient (
                                   recipe_id INTEGER REFERENCES recipe(id) ON DELETE CASCADE,
                                   ingredient_id INTEGER REFERENCES ingredient(id) ON DELETE CASCADE,
                                   PRIMARY KEY (recipe_id, ingredient_id)
);

-- =======================
-- DIETARY TAG
-- =======================
CREATE TABLE dietary_tag (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL UNIQUE
);

-- =======================
-- RECIPE_DIETARY_TAG (M-M)
-- =======================
CREATE TABLE recipe_dietary_tag (
                                    recipe_id INTEGER REFERENCES recipe(id) ON DELETE CASCADE,
                                    dietary_tag_id INTEGER REFERENCES dietary_tag(id) ON DELETE CASCADE,
                                    PRIMARY KEY (recipe_id, dietary_tag_id)
);

-- =======================
-- USER_FAVORITES (M-M)
-- =======================
CREATE TABLE user_favorite_recipes (
                                       user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                                       recipe_id INTEGER REFERENCES recipe(id) ON DELETE CASCADE,
                                       PRIMARY KEY (user_id, recipe_id)
);

-- =======================
-- MEAL PLAN
-- =======================
CREATE TABLE meal_plan (
                           id SERIAL PRIMARY KEY,
                           user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                           start_date DATE,
                           end_date DATE
);

-- =======================
-- MEAL PLAN ENTRY
-- =======================
CREATE TABLE meal_plan_entry (
                                 id SERIAL PRIMARY KEY,
                                 meal_plan_id INTEGER REFERENCES meal_plan(id) ON DELETE CASCADE,
                                 recipe_id INTEGER REFERENCES recipe(id),
                                 meal_date DATE,
                                 meal_type VARCHAR(20)
);

-- =======================
-- FEEDBACK
-- =======================
CREATE TABLE feedback (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                          rating INTEGER,
                          message TEXT,
                          wants_newsletter BOOLEAN
);