SET search_path TO project;

ALTER TABLE profile
    ADD COLUMN IF NOT EXISTS meals_per_day INTEGER DEFAULT 3;

UPDATE profile
SET meals_per_day = 3
WHERE meals_per_day IS NULL;

ALTER TABLE profile
    ALTER COLUMN meals_per_day SET NOT NULL;

ALTER TABLE profile
    DROP CONSTRAINT IF EXISTS profile_meals_per_day_chk;

ALTER TABLE profile
    ADD CONSTRAINT profile_meals_per_day_chk CHECK (meals_per_day >= 2 AND meals_per_day <= 10);

-- Slot 0 .. meals_per_day-1 pentru fiecare zi (în loc să fie doar BREAKFAST/LUNCH/… fix)
ALTER TABLE meal_plan_entry
    ADD COLUMN IF NOT EXISTS meal_slot_index INTEGER DEFAULT 0;

UPDATE meal_plan_entry
SET meal_slot_index = 0
WHERE meal_slot_index IS NULL;

ALTER TABLE meal_plan_entry
    ALTER COLUMN meal_slot_index SET NOT NULL;

DELETE FROM meal_plan_entry a
    USING meal_plan_entry b
WHERE a.meal_plan_id = b.meal_plan_id
  AND a.meal_date = b.meal_date
  AND a.meal_slot_index = b.meal_slot_index
  AND a.id > b.id;

CREATE UNIQUE INDEX IF NOT EXISTS meal_plan_entry_plan_date_slot_uidx
    ON meal_plan_entry (meal_plan_id, meal_date, meal_slot_index);
