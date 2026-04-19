SET search_path TO project;

-- Seed 10 recipes (idempotent by title)
INSERT INTO recipe (title, description, calories, protein, carbs, fat, cooking_time, difficulty)
SELECT v.title,
       v.description,
       v.calories,
       v.protein,
       v.carbs,
       v.fat,
       v.cooking_time,
       v.difficulty
FROM (VALUES
    ('Grilled Chicken Bowl', 'Grilled chicken with brown rice and vegetables.', 620, 48.0, 58.0, 18.0, 30, 'EASY'),
    ('Vegan Buddha Bowl', 'Quinoa, chickpeas, avocado and mixed greens.', 540, 18.0, 62.0, 22.0, 25, 'EASY'),
    ('Keto Omelette', 'Egg omelette with cheese, spinach and mushrooms.', 430, 28.0, 6.0, 32.0, 15, 'EASY'),
    ('Salmon with Asparagus', 'Baked salmon fillet served with asparagus.', 510, 40.0, 10.0, 34.0, 28, 'MEDIUM'),
    ('Turkey Meatballs', 'Turkey meatballs in tomato sauce with zucchini noodles.', 560, 44.0, 24.0, 30.0, 40, 'MEDIUM'),
    ('Lentil Curry', 'Red lentil curry with coconut milk and spices.', 590, 22.0, 68.0, 24.0, 35, 'MEDIUM'),
    ('Beef Stir Fry', 'Lean beef stir-fried with broccoli and peppers.', 650, 46.0, 34.0, 31.0, 27, 'MEDIUM'),
    ('Greek Yogurt Pancakes', 'High protein pancakes with greek yogurt base.', 480, 26.0, 49.0, 18.0, 20, 'EASY'),
    ('Tofu Veggie Wrap', 'Tofu wrap with hummus and crunchy vegetables.', 520, 21.0, 57.0, 22.0, 18, 'EASY'),
    ('Shrimp Quinoa Salad', 'Shrimp salad with quinoa, cucumber and lemon dressing.', 500, 36.0, 42.0, 20.0, 22, 'EASY')
) AS v(title, description, calories, protein, carbs, fat, cooking_time, difficulty)
WHERE NOT EXISTS (
    SELECT 1
    FROM recipe r
    WHERE r.title = v.title
);

-- Link seeded recipes to dietary tags (idempotent by PK in recipe_dietary_tag)
INSERT INTO recipe_dietary_tag (recipe_id, dietary_tag_id)
SELECT r.id, t.id
FROM recipe r
JOIN dietary_tag t ON t.name = 'GLUTEN_FREE'
WHERE r.title IN ('Grilled Chicken Bowl', 'Keto Omelette', 'Salmon with Asparagus', 'Turkey Meatballs', 'Beef Stir Fry', 'Shrimp Quinoa Salad')
ON CONFLICT (recipe_id, dietary_tag_id) DO NOTHING;

INSERT INTO recipe_dietary_tag (recipe_id, dietary_tag_id)
SELECT r.id, t.id
FROM recipe r
JOIN dietary_tag t ON t.name = 'VEGAN'
WHERE r.title IN ('Vegan Buddha Bowl', 'Lentil Curry', 'Tofu Veggie Wrap')
ON CONFLICT (recipe_id, dietary_tag_id) DO NOTHING;

INSERT INTO recipe_dietary_tag (recipe_id, dietary_tag_id)
SELECT r.id, t.id
FROM recipe r
JOIN dietary_tag t ON t.name = 'VEGETARIAN'
WHERE r.title IN ('Greek Yogurt Pancakes', 'Lentil Curry')
ON CONFLICT (recipe_id, dietary_tag_id) DO NOTHING;

INSERT INTO recipe_dietary_tag (recipe_id, dietary_tag_id)
SELECT r.id, t.id
FROM recipe r
JOIN dietary_tag t ON t.name = 'KETO'
WHERE r.title IN ('Keto Omelette', 'Salmon with Asparagus')
ON CONFLICT (recipe_id, dietary_tag_id) DO NOTHING;

INSERT INTO recipe_dietary_tag (recipe_id, dietary_tag_id)
SELECT r.id, t.id
FROM recipe r
JOIN dietary_tag t ON t.name = 'LACTOSE_FREE'
WHERE r.title IN ('Grilled Chicken Bowl', 'Vegan Buddha Bowl', 'Lentil Curry', 'Beef Stir Fry', 'Tofu Veggie Wrap', 'Shrimp Quinoa Salad')
ON CONFLICT (recipe_id, dietary_tag_id) DO NOTHING;

