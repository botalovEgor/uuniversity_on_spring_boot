DROP TABLE IF EXISTS schedule CASCADE;
DROP TABLE IF EXISTS course_teacher CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS lectureHalls CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS course_trainingProgram CASCADE;
DROP TABLE IF EXISTS trainingProgram CASCADE;
DROP TABLE IF EXISTS courses CASCADE;



CREATE TABLE courses
(
    ID    SERIAL PRIMARY KEY,
    name  CHARACTER VARYING(30) constraint name_already_use UNIQUE,
    hours INTEGER
);

CREATE TABLE trainingProgram
(
    ID    SERIAL PRIMARY KEY,
    speciality CHARACTER VARYING(30)  constraint speciality_name_already_use UNIQUE
);

CREATE TABLE course_trainingProgram
(
    course_id          integer REFERENCES courses (id) ON DELETE CASCADE,
    trainingProgram_id INTEGER REFERENCES trainingProgram (Id) ON DELETE CASCADE,
    CONSTRAINT program_already_contains_course UNIQUE (course_id, trainingProgram_id)

);

CREATE TABLE groups
(
    ID                 SERIAL PRIMARY KEY,
    description        CHARACTER VARYING(30) constraint description_name_already_use UNIQUE,
    trainingProgram_id INTEGER REFERENCES trainingProgram (id) ON DELETE CASCADE
);

CREATE TABLE lectureHalls
(
    ID      SERIAL PRIMARY KEY,
    housing INTEGER,
    floor   INTEGER,
    number  INTEGER,
    CONSTRAINT this_lectureHall_already_exists UNIQUE (housing, floor, number)
);

CREATE TABLE students
(
    ID    SERIAL PRIMARY KEY,
    first_name CHARACTER VARYING(30),
    last_name  CHARACTER VARYING(30),
    group_id   integer REFERENCES groups (id) ON DELETE CASCADE
);

CREATE TABLE teachers
(
    ID    SERIAL PRIMARY KEY,
    first_name CHARACTER VARYING(30),
    last_name  CHARACTER VARYING(30)
);

CREATE TABLE course_teacher
(
    course_id  INTEGER REFERENCES courses (id) ON DELETE CASCADE,
    teacher_id INTEGER REFERENCES teachers (id) ON DELETE CASCADE,
    CONSTRAINT teacher_already_teaching_course UNIQUE (course_id, teacher_id)
);

CREATE TABLE schedule
(
    ID             SERIAL PRIMARY KEY,
    lesson_date    DATE,
    lesson_time    TIME,
    lectureHall_id INTEGER REFERENCES lectureHalls (Id) ON DELETE CASCADE,
    group_id       INTEGER REFERENCES groups (Id) ON DELETE CASCADE,
    teacher_id     INTEGER REFERENCES teachers (Id) ON DELETE CASCADE,
    course_id      INTEGER REFERENCES courses (Id) ON DELETE CASCADE,
    CONSTRAINT teacher_already_teaching_in_this_time UNIQUE (lesson_date, lesson_time, teacher_id),
    CONSTRAINT lectureHall_already_using_in_this_time UNIQUE (lesson_date, lesson_time, lectureHall_id),
    CONSTRAINT group_already_studying_in_this_time UNIQUE (lesson_date, lesson_time, teacher_id)
);