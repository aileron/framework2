set @value=/*#{var value}*/'0'/*#*/;
create table if not exists test
(
    id int unsigned not null primary key auto_increment
);
create table if not exists test_c
(
    id int unsigned not null primary key,
    value varchar(255) not null
);
insert into test () values ();
set @id = last_insert_id();
insert into test_c (id,value) values 
(
    cast(@id as int unsigned)
    ,@value
);
select @id;