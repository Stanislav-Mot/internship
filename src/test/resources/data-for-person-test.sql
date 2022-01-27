INSERT INTO person (firstname, lastname, birthdate)
VALUES ('GetTester', 'Tester', '1984-03-03'),
       ('UpdateTester', 'Tester', '1984-03-03'),
       ('DeleteGroupTester', 'Tester', '1984-03-03'),
       ('DeleteTester', 'Tester', '1984-03-03');

INSERT INTO group_of_tasks (name)
VALUES ('testGroup'),
       ('secondGroup'),
       ('thirdGroup'),
       ('fourGroup');

INSERT INTO person_group(id_person, id_group)
VALUES (1, 1),
       (3, 3),
       (1, 3),
       (1, 4);


