create table if not exists product_item(
    id int  primary key ,
    name varchar(64) NOT NULL,
    pid int NOT NULL
);