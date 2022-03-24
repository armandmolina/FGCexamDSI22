DROP TABLE if EXISTS user;
create table user (
    username varchar(255) not null,
    name varchar(255),
    second_name varchar(255),
    email varchar(255),
    primary key (username));

DROP TABLE if EXISTS friend;
create table friend (
    username varchar(255) not null,
    friend varchar(255) not null,
    PRIMARY KEY (username, friend),
    FOREIGN KEY (username) REFERENCES user(username)
);

alter table user add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);
