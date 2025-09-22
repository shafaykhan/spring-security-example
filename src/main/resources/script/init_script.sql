create database if not exists spring_security_db_2;
use spring_security_db_2;

drop table if exists employee;
create table employee
(
    id       int auto_increment primary key,
    name     varchar(100) not null,
    email    varchar(255) null,
    username varchar(12)  not null,
    password varchar(100) not null,
    constraint employee_email_uindex unique (email),
    constraint employee_username_uindex unique (username)
);

insert into employee (id, name, email, username, password)
values (1, 'user 1', null, 'user1', '$2a$12$sacfL/q2Zsw/SAVa3lBu4.qTDn0rq9wItO7lyaoNinF7WF74hmOo.'),
       (2, 'user 2', null, 'user2', '$2a$12$sacfL/q2Zsw/SAVa3lBu4.qTDn0rq9wItO7lyaoNinF7WF74hmOo.');