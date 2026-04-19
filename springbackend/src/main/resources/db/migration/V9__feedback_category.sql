SET search_path TO project;

ALTER TABLE feedback
    ADD COLUMN IF NOT EXISTS category VARCHAR(50);

