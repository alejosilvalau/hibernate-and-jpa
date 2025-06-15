CREATE TABLE IF NOT EXISTS users (id IDENTITY PRIMARY KEY, name VARCHAR(255), birth_date DATE);

INSERT INTO users (name, birth_date) VALUES ('marco', '1950-01-01');
INSERT INTO users (name, birth_date) VALUES ('ocram', '1960-01-01');