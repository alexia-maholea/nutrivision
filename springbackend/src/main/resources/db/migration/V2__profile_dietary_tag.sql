-- Baze vechi: V1 rulat înainte să existe tabela M–M profile ↔ dietary_tag.
-- Idempotent: pe o instalare nouă cu V1 complet, CREATE IF NOT EXISTS nu face nimic.

CREATE SCHEMA IF NOT EXISTS project;

SET search_path TO project;

CREATE TABLE IF NOT EXISTS profile_dietary_tag (
    profile_id      BIGINT NOT NULL REFERENCES profile (id) ON DELETE CASCADE,
    dietary_tag_id  BIGINT NOT NULL REFERENCES dietary_tag (id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, dietary_tag_id)
);
