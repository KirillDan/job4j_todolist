create table item (
    int id serial primary key,
    desc varchar(255),
    created timestamp,
    done boolean
);