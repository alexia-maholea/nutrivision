SET search_path TO project;

-- Seed ingredient catalog (idempotent by case-insensitive name)
INSERT INTO ingredient (name, calories_per_100g)
SELECT v.name, v.calories_per_100g
FROM (VALUES
    ('Chicken breast', 165.0),
    ('Brown rice', 112.0),
    ('Broccoli', 34.0),
    ('Olive oil', 884.0),
    ('Quinoa', 120.0),
    ('Chickpeas', 164.0),
    ('Avocado', 160.0),
    ('Mixed greens', 20.0),
    ('Egg', 155.0),
    ('Cheese', 402.0),
    ('Spinach', 23.0),
    ('Mushrooms', 22.0),
    ('Salmon', 208.0),
    ('Asparagus', 20.0),
    ('Lemon', 29.0),
    ('Turkey mince', 170.0),
    ('Tomato sauce', 80.0),
    ('Zucchini', 17.0),
    ('Garlic', 149.0),
    ('Red lentils', 116.0),
    ('Coconut milk', 230.0),
    ('Onion', 40.0),
    ('Curry powder', 325.0),
    ('Lean beef', 250.0),
    ('Bell pepper', 31.0),
    ('Soy sauce', 53.0),
    ('Greek yogurt', 59.0),
    ('Oat flour', 404.0),
    ('Baking powder', 53.0),
    ('Tofu', 144.0),
    ('Whole wheat tortilla', 290.0),
    ('Hummus', 166.0),
    ('Shrimp', 99.0),
    ('Cucumber', 15.0)
) AS v(name, calories_per_100g)
WHERE NOT EXISTS (
    SELECT 1
    FROM ingredient i
    WHERE LOWER(i.name) = LOWER(v.name)
);

-- Link recipes to ingredients with quantity/unit
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit)
SELECT r.id, i.id, v.quantity, v.unit
FROM (VALUES
    ('Grilled Chicken Bowl', 'Chicken breast', 180.0, 'g'),
    ('Grilled Chicken Bowl', 'Brown rice', 200.0, 'g'),
    ('Grilled Chicken Bowl', 'Broccoli', 120.0, 'g'),
    ('Grilled Chicken Bowl', 'Olive oil', 10.0, 'ml'),

    ('Vegan Buddha Bowl', 'Quinoa', 180.0, 'g'),
    ('Vegan Buddha Bowl', 'Chickpeas', 140.0, 'g'),
    ('Vegan Buddha Bowl', 'Avocado', 70.0, 'g'),
    ('Vegan Buddha Bowl', 'Mixed greens', 80.0, 'g'),

    ('Keto Omelette', 'Egg', 180.0, 'g'),
    ('Keto Omelette', 'Cheese', 40.0, 'g'),
    ('Keto Omelette', 'Spinach', 60.0, 'g'),
    ('Keto Omelette', 'Mushrooms', 80.0, 'g'),

    ('Salmon with Asparagus', 'Salmon', 200.0, 'g'),
    ('Salmon with Asparagus', 'Asparagus', 180.0, 'g'),
    ('Salmon with Asparagus', 'Olive oil', 10.0, 'ml'),
    ('Salmon with Asparagus', 'Lemon', 30.0, 'g'),

    ('Turkey Meatballs', 'Turkey mince', 220.0, 'g'),
    ('Turkey Meatballs', 'Tomato sauce', 160.0, 'g'),
    ('Turkey Meatballs', 'Zucchini', 180.0, 'g'),
    ('Turkey Meatballs', 'Garlic', 8.0, 'g'),

    ('Lentil Curry', 'Red lentils', 170.0, 'g'),
    ('Lentil Curry', 'Coconut milk', 120.0, 'ml'),
    ('Lentil Curry', 'Onion', 90.0, 'g'),
    ('Lentil Curry', 'Curry powder', 8.0, 'g'),

    ('Beef Stir Fry', 'Lean beef', 200.0, 'g'),
    ('Beef Stir Fry', 'Broccoli', 130.0, 'g'),
    ('Beef Stir Fry', 'Bell pepper', 100.0, 'g'),
    ('Beef Stir Fry', 'Soy sauce', 20.0, 'ml'),

    ('Greek Yogurt Pancakes', 'Greek yogurt', 140.0, 'g'),
    ('Greek Yogurt Pancakes', 'Egg', 100.0, 'g'),
    ('Greek Yogurt Pancakes', 'Oat flour', 90.0, 'g'),
    ('Greek Yogurt Pancakes', 'Baking powder', 5.0, 'g'),

    ('Tofu Veggie Wrap', 'Tofu', 150.0, 'g'),
    ('Tofu Veggie Wrap', 'Whole wheat tortilla', 70.0, 'g'),
    ('Tofu Veggie Wrap', 'Hummus', 50.0, 'g'),
    ('Tofu Veggie Wrap', 'Bell pepper', 80.0, 'g'),

    ('Shrimp Quinoa Salad', 'Shrimp', 180.0, 'g'),
    ('Shrimp Quinoa Salad', 'Quinoa', 150.0, 'g'),
    ('Shrimp Quinoa Salad', 'Cucumber', 120.0, 'g'),
    ('Shrimp Quinoa Salad', 'Lemon', 25.0, 'g')
) AS v(recipe_title, ingredient_name, quantity, unit)
JOIN recipe r ON r.title = v.recipe_title
JOIN ingredient i ON LOWER(i.name) = LOWER(v.ingredient_name)
ON CONFLICT (recipe_id, ingredient_id) DO UPDATE
SET quantity = EXCLUDED.quantity,
    unit = EXCLUDED.unit;

-- Seed preparation steps (ordered)
INSERT INTO recipe_step (recipe_id, step_no, description, duration_minutes)
SELECT r.id, v.step_no, v.description, v.duration_minutes
FROM (VALUES
    ('Grilled Chicken Bowl', 1, 'Season chicken breast and grill until cooked through.', 12),
    ('Grilled Chicken Bowl', 2, 'Boil brown rice and steam broccoli.', 15),
    ('Grilled Chicken Bowl', 3, 'Assemble bowl and drizzle olive oil.', 3),

    ('Vegan Buddha Bowl', 1, 'Cook quinoa and let it cool slightly.', 12),
    ('Vegan Buddha Bowl', 2, 'Rinse chickpeas and slice avocado.', 6),
    ('Vegan Buddha Bowl', 3, 'Combine all ingredients over mixed greens.', 4),

    ('Keto Omelette', 1, 'Saute mushrooms and spinach in a non-stick pan.', 6),
    ('Keto Omelette', 2, 'Add beaten eggs and cook until almost set.', 4),
    ('Keto Omelette', 3, 'Top with cheese, fold, and finish cooking.', 3),

    ('Salmon with Asparagus', 1, 'Season salmon and asparagus with lemon and olive oil.', 5),
    ('Salmon with Asparagus', 2, 'Bake salmon and asparagus in preheated oven.', 20),
    ('Salmon with Asparagus', 3, 'Plate and serve immediately.', 3),

    ('Turkey Meatballs', 1, 'Mix turkey mince with garlic and shape meatballs.', 8),
    ('Turkey Meatballs', 2, 'Brown meatballs, then simmer in tomato sauce.', 20),
    ('Turkey Meatballs', 3, 'Spiralize zucchini and serve with meatballs.', 10),

    ('Lentil Curry', 1, 'Saute onion with curry powder until fragrant.', 6),
    ('Lentil Curry', 2, 'Add lentils and coconut milk, then simmer.', 22),
    ('Lentil Curry', 3, 'Adjust seasoning and serve hot.', 4),

    ('Beef Stir Fry', 1, 'Slice beef and marinate briefly with soy sauce.', 6),
    ('Beef Stir Fry', 2, 'Stir-fry beef on high heat until browned.', 7),
    ('Beef Stir Fry', 3, 'Add vegetables and cook until tender-crisp.', 8),

    ('Greek Yogurt Pancakes', 1, 'Whisk greek yogurt, eggs, and dry ingredients.', 6),
    ('Greek Yogurt Pancakes', 2, 'Cook pancakes on a lightly greased skillet.', 10),
    ('Greek Yogurt Pancakes', 3, 'Serve warm with desired toppings.', 2),

    ('Tofu Veggie Wrap', 1, 'Pan-sear tofu cubes until golden.', 8),
    ('Tofu Veggie Wrap', 2, 'Warm tortilla and spread hummus.', 3),
    ('Tofu Veggie Wrap', 3, 'Add tofu and vegetables, then roll wrap.', 4),

    ('Shrimp Quinoa Salad', 1, 'Cook quinoa and chill.', 12),
    ('Shrimp Quinoa Salad', 2, 'Saute shrimp with lemon zest.', 6),
    ('Shrimp Quinoa Salad', 3, 'Mix quinoa, shrimp, cucumber, and lemon juice.', 4)
) AS v(recipe_title, step_no, description, duration_minutes)
JOIN recipe r ON r.title = v.recipe_title
ON CONFLICT (recipe_id, step_no) DO UPDATE
SET description = EXCLUDED.description,
    duration_minutes = EXCLUDED.duration_minutes;

