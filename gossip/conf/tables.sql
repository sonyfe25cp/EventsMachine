create table words(
	id int NOT NULL AUTO_INCREMENT,
	name varchar(45),
	count int,
	idf double,
	PRIMARY KEY (id)
);