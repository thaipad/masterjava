DROP TABLE IF EXISTS groups;
DROP SEQUENCE IF EXISTS groups_seq;
DROP TYPE IF EXISTS group_type;
DROP TABLE IF EXISTS projects;
DROP SEQUENCE IF EXISTS projects_seq;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS cities_seq;

CREATE SEQUENCE cities_seq START 100000;
CREATE TABLE cities (
  id      INTEGER PRIMARY KEY DEFAULT nextval('cities_seq'),
  name    TEXT NOT NULL
);

CREATE SEQUENCE projects_seq START 100000;
CREATE TABLE projects (
  id      INTEGER PRIMARY KEY DEFAULT nextval('projects_seq'),
  name    TEXT NOT NULL,
  description TEXT
);

CREATE TYPE group_type AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');
CREATE SEQUENCE groups_seq START 100000;
CREATE TABLE groups (
  id      INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
  name    TEXT NOT NULL,
  type    group_type NOT NULL,
  id_project INTEGER REFERENCES projects(id)
);

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE SEQUENCE user_seq START 100000;
CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  id_city   INTEGER REFERENCES cities(id)
);

CREATE UNIQUE INDEX email_idx ON users (email);

