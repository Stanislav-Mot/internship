CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    password VARCHAR(256) NOT NULL,
    email    VARCHAR(256) NOT NULL
);

CREATE TABLE user_role
(
    user_id INT8 NOT NULL,
    role    varchar(255),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (email, password)
VALUES ('admin@admin.com', 'admin');

INSERT INTO user_role(user_id, role)
VALUES (1, 'ROLE_ADMIN');

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE users
SET password = crypt(password, gen_salt('bf', 8));