DROP TABLE if EXISTS day_time_start;
create table day_time_start (
    daytime_id varchar(255),
    timeStart varchar(255),
    day_of_week varchar(255),
    favorite_journey_id varchar(255) not null,
    primary key (daytime_id));

DROP TABLE if EXISTS favorite_journey;
create table favorite_journey (
    favorite_journey_id varchar(255),
    journey varchar(255),
    user_id varchar(255) not null,
    checked BOOLEAN DEFAULT TRUE,
    primary key (favorite_journey_id));

DROP TABLE if EXISTS journey;
create table journey (
    journey_id varchar(255),
    destination varchar(255),
    origin varchar(255),
    primary key (journey_id));

DROP TABLE if EXISTS station;
create table station (
    latitud varchar(255),
    longitud varchar(255),
    nom varchar(255),
    primary key (nom));

alter table day_time_start add constraint FKpy2gs5fcjrgf6gvyms4df848u foreign key (favorite_journey_id) references favorite_journey;
alter table favorite_journey add constraint FK8xg4pcjrih9o5j77scnlr6n9e foreign key (journey) references journey;
alter table journey add constraint FKo1t1t6c4tb55wa8v2p5k7oalq foreign key (destination) references station;
alter table journey add constraint FKgjb6wxqccbk6tqmtq72wp30q2 foreign key (origin) references station;

