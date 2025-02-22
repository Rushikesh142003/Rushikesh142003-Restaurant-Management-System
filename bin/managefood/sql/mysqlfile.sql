-- Creating database
CREATE DATABASE IF NOT EXISTS restaurant;

-- Selecting the database
use restaurant;

-- Creating food table
CREATE TABLE IF NOT EXISTS food (
  id int primary key,
  food_name varchar(25) not null,
  price int not null
);

-- Creating orderfoodrecord table
CREATE TABLE if not exists orderfoodrecord (
    bill_number INT,
    food_id INT NOT NULL,
    foodname VARCHAR(255) NOT NULL,
    foodprize DOUBLE NOT NULL,
    food_quantity INT NOT NULL,
    date DATE NOT NULL,
    time VARCHAR(10) NOT NULL  -- Use VARCHAR to store time in "hh:mm am/pm" format
);

-- Creating foodtable table
create table foodtable(
consumer varchar (30),
table_number varchar(20) primary key,
availability varchar(20) Not null,
table_type varchar(30) not null
);



-- creating billno table
create table if not exists billno( bill_number_increment int );

-- insert data 0 into billno table because it need to increment billno otherwise not work properly on java code
insert into billno values(0);



-- Create admin table
create  table if not exists  admin (
userid varchar(25) primary key,
userpassword varchar(25)
);

-- Inserting value in admin table
insert into admin values
("admin","admin");
-- create  employee table  to store employee information
 create table employee(
 eid varchar(10) not null,
name varchar(40) not null,
age varchar(10) not null,
gender varchar(10) not null,
job varchar(40) not null,
salary varchar(15) not null,
phone varchar(15) not null,
aadhar varchar(20) not null,
email varchar(40) not null
);
select * from admin;
select * from food;
select * from foodtable;
select * from orderfoodrecord;
select * from billno;
select * from employee;
truncate orderfoodrecord;
truncate billno;
truncate employee;
drop table employee;
insert into orderfoodrecord values(5,1,'rayta',39,2,'2023-04-01','08:28 am');