DO $$
DECLARE
    message_type text;
BEGIN
    SELECT data_type
    INTO message_type
    FROM information_schema.columns
    WHERE table_schema = 'project'
      AND table_name = 'feedback'
      AND column_name = 'message';

    IF message_type = 'bytea' THEN
        ALTER TABLE project.feedback
            ALTER COLUMN message TYPE TEXT
            USING convert_from(message, 'UTF8');
    END IF;
END $$;

