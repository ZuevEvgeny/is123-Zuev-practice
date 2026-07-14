-- Кафедры/факультеты
INSERT INTO department (id, title) VALUES (1, 'Радиотехнический');
INSERT INTO department (id, title) VALUES (2, 'Информационных технологий');
INSERT INTO department (id, title) VALUES (3, 'Экономический');

-- Студенты
INSERT INTO student (id, name, age, department_id) VALUES (1, 'Михаил Иванов', 20, 1);
INSERT INTO student (id, name, age, department_id) VALUES (2, 'Мария Петрова', 19, 2);
INSERT INTO student (id, name, age, department_id) VALUES (3, 'Магомед Алиев', 21, 1);
INSERT INTO student (id, name, age, department_id) VALUES (4, 'Анна Смирнова', 18, 2);
INSERT INTO student (id, name, age, department_id) VALUES (5, 'Иван Кузнецов', 20, 3);