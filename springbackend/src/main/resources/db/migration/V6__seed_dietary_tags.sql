SET search_path TO project;

INSERT INTO dietary_tag (name)
VALUES ('VEGAN'),
       ('VEGETARIAN'),
       ('GLUTEN_FREE'),
       ('KETO'),
       ('LACTOSE_FREE')
ON CONFLICT (name) DO NOTHING;

