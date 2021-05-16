create database if not exists spring_security_db;

use spring_security_db;

CREATE TABLE users
(
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  TINYINT      NOT NULL DEFAULT 1,
    PRIMARY KEY (username)
);

CREATE TABLE authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX ix_auth_username
    on authorities (username, authority);

INSERT INTO users (username, password, enabled)
values ('user', '$2y$12$jgkzysfZMj0jY5VlWiZe.OnHgXEmIe4m3OxdT4ugJHeCyXy8Qn/Oa', 1), -- password -- user
       ('admin', '$2y$12$pfjHhp4Cx6Jv7uZQHZud/.TJCP9rT8Oi5Yb7WNnAcJqtwEshlLz1W', 1); -- password -- admin

INSERT INTO authorities (username, authority)
values ('user', 'USER'),
       ('admin', 'ADMIN');
