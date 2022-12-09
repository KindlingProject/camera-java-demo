CREATE TABLE if not exists weather (
  id int(11) NOT NULL,
  city varchar(80),
  temp_lo int(11),
  temp_hi int(11),
  prcpe double,
  create_time date,
  PRIMARY KEY (id)
);

insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(1, 'Beijing', -20, -5, 10.3, '2018-12-31');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(2, 'ShangHai', 0, 3, 10.3, '2018-12-31');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(3, 'ShenZhen', 5, 15, 10, '2018-12-31');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(4, 'SanYa', 25, 40, 10, '2018-12-31');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(5, 'ChongQin', 10, 15, 0, '2018-12-31');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(6, 'ChangSha', 25, 30, 0, '2019-01-29');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(7, 'XiAn', 15, 18, 200.3, '2019-06-12');
insert into weather(id, city, temp_lo, temp_hi, prcpe, create_time) values(8, 'NanJing', 30, 35, 0.3, '2019-07-25');


