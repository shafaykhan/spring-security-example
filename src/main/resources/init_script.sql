create database if not exists spring_security_db;

use spring_security_db;

-- --Jdbc_Auth_With_Custom_Schema----
drop table if exists user;
create table user
(
    id       int auto_increment primary key,
    name     varchar(50)                 not null,
    password varchar(100)                not null,
    role     varchar(100)                not null,
    status   enum ('ACTIVE', 'INACTIVE') not null,
    constraint user_name_uindex unique (name),
    constraint user_id_name_uindex unique (name, role)
);

insert into user(name, password, role, status)
values ('user', '$2y$12$jgkzysfZMj0jY5VlWiZe.OnHgXEmIe4m3OxdT4ugJHeCyXy8Qn/Oa', 'ROLE_USER', 'ACTIVE'), -- password - user
       ('admin', '$2y$12$pfjHhp4Cx6Jv7uZQHZud/.TJCP9rT8Oi5Yb7WNnAcJqtwEshlLz1W', 'ROLE_ADMIN', 'ACTIVE'); -- password - admin
