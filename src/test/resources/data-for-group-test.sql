INSERT INTO person (first_name, last_name, birthdate)
VALUES ('GetTester', 'Tester', '1967-11-11'),
       ('UpdateTester', 'Tester', '1967-11-11'),
       ('DeleteGroupTester', 'Tester', '1967-11-11'),
       ('DeleteTester', 'Tester', '1967-11-11');

INSERT INTO task (name, start_time)
VALUES ('cleaning', '2021-06-09'),
       ('to_swim', '2012-06-09'),
       ('do_something', '2012-05-06'),
       ('for_delete', '2011-11-11');

INSERT INTO groups (name)
VALUES ('forGet'),
       ('second'),
       ('forUpdate'),
       ('forDelete');

UPDATE task
set id_group = 1;

update groups
set id_parent = 1
where id = 3;
