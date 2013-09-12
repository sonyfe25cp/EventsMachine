create database gossip;
create table event(id int auto_increment primary key,
	title varchar(50) not null,
	recommended float not null , 
	 keywords varchar(50),
	 pages varchar(999),
	 create_time datetime,
	 content_abstract varchar(999));
insert into event(title,recommended,keywords,pages,create_time) values("title4",1.5,"key1;key3","411;123;913;114;615",now());
