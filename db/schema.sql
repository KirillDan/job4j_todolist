create table item (
    id serial primary key,
    description varchar(255),
    created timestamp,
    done boolean,
    author int not null references j_user(id)
);

create table j_role (
    id serial primary key,
    name varchar(2000)
);

CREATE TABLE j_user (
	id SERIAL PRIMARY KEY,
	name TEXT,
	email TEXT,
	password TEXT,
	role_id int not null references j_role(id)
);
