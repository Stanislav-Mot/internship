INSERT INTO person (email, password, first_name, last_name, birthdate)
VALUES ('admin@admin.com', 'admin', 'Admin', 'Adminov', '1967-05-10');

INSERT INTO user_role(user_id, role)
VALUES (1, 'ROLE_ADMIN');

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE person
SET password = crypt(password, gen_salt('bf', 8));