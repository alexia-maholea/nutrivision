-- Dacă V2 nu s-a aplicat (ex. FK bigint vs coloane integer dintr-o bază veche), creează tabela
-- cu tipuri care se potrivesc exact cu project.profile(id) și project.dietary_tag(id).
-- Dacă tabela există deja (V2 OK), nu face nimic.

CREATE SCHEMA IF NOT EXISTS project;

DO $$
DECLARE
    pid_type text;
    tid_type text;
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'project'
          AND table_name = 'profile_dietary_tag'
    ) THEN
        RETURN;
    END IF;

    SELECT pg_catalog.format_type(a.atttypid, a.atttypmod)
    INTO pid_type
    FROM pg_catalog.pg_attribute a
             JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
             JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
    WHERE n.nspname = 'project'
      AND c.relname = 'profile'
      AND a.attname = 'id'
      AND a.attnum > 0
      AND NOT a.attisdropped;

    SELECT pg_catalog.format_type(a.atttypid, a.atttypmod)
    INTO tid_type
    FROM pg_catalog.pg_attribute a
             JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
             JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
    WHERE n.nspname = 'project'
      AND c.relname = 'dietary_tag'
      AND a.attname = 'id'
      AND a.attnum > 0
      AND NOT a.attisdropped;

    IF pid_type IS NULL OR tid_type IS NULL THEN
        RAISE EXCEPTION 'V3: lipsește project.profile sau project.dietary_tag';
    END IF;

    EXECUTE format(
            'CREATE TABLE project.profile_dietary_tag (
                profile_id %s NOT NULL REFERENCES project.profile(id) ON DELETE CASCADE,
                dietary_tag_id %s NOT NULL REFERENCES project.dietary_tag(id) ON DELETE CASCADE,
                PRIMARY KEY (profile_id, dietary_tag_id)
            )',
            pid_type,
            tid_type
            );
END;
$$;
