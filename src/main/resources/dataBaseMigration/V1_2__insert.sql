INSERT INTO courses (name, hours)
VALUES ('course_1', 1),
       ('course_2', 2),
       ('course_3', 3);

INSERT INTO trainingProgram (speciality)
VALUES ('trainingProgram_1'),
       ('trainingProgram_2'),
       ('trainingProgram_3');

INSERT INTO course_trainingProgram (course_id, trainingProgram_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO groups (description, trainingProgram_id)
VALUES ('group_1', 1),
       ('group_2', 2),
       ('group_3', 3);

INSERT INTO lectureHalls (housing, floor, number)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3);

INSERT INTO students (first_name, last_name, group_id)
VALUES ('student_1', 'student_1', 1),
       ('student_2', 'student_2', 2),
       ('student_3', 'student_3', 3);

INSERT INTO teachers (first_name, last_name)
VALUES ('teacher_1', 'teacher_1_lastName'),
       ('teacher_2', 'teacher_2_lastName'),
       ('teacher_3', 'teacher_3_lastName');

INSERT INTO course_teacher (course_id, teacher_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO schedule (lesson_date, lesson_time, lectureHall_id, group_id, teacher_id, course_id)
VALUES ('2020-01-01', '01:01', 1, 1, 1, 1),
       ('2020-02-02', '02:02', 2, 2, 2, 2),
       ('2020-03-03', '03:03', 3, 3, 3, 3);