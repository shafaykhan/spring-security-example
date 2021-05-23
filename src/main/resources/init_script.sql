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
values ('user', 'user', 'ROLE_USER', 'ACTIVE'), -- password - user
       ('admin', 'admin', 'ROLE_ADMIN', 'ACTIVE'); -- password - admin
