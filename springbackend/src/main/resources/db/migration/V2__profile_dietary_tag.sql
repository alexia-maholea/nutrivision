CREATE SCHEMA IF NOT EXISTS project;

SET search_path TO project;

CREATE TABLE IF NOT EXISTS profile_dietary_tag (
    profile_id      BIGINT NOT NULL REFERENCES profile (id) ON DELETE CASCADE,
    dietary_tag_id  BIGINT NOT NULL REFERENCES dietary_tag (id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, dietary_tag_id)
);
