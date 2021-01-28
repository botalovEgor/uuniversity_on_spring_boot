package my.project.university.repositoty;

import my.project.university.models.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Component
public class TestData {

    public Course course1;
    public Course course2;
    public Course course3;
    public List<Course> allCourses;

    public TrainingProgram trainingProgram1;
    public TrainingProgram trainingProgram2;
    public TrainingProgram trainingProgram3;
    public List<TrainingProgram> allTrainingPrograms;

    public Group group1;
    public Group group2;
    public Group group3;
    public List<Group> allGroups;

    public Student student1;
    public Student student2;
    public Student student3;
    public List<Student> allStudents;

    public Teacher teacher1;
    public Teacher teacher2;
    public Teacher teacher3;
    public List<Teacher> allTeachers;

    public LectureHall lectureHall1;
    public LectureHall lectureHall2;
    public LectureHall lectureHall3;
    public List<LectureHall> allLectureHalls;

    public Schedule schedule1;
    public Schedule schedule2;
    public Schedule schedule3;
    public List<Schedule> allSchedules;

    public void fillTestData() {
        course1 = new Course(1, "course_1", 1);
        course2 = new Course(2, "course_2", 2);
        course3 = new Course(3, "course_3", 3);

        trainingProgram1 = new TrainingProgram(1, "trainingProgram_1");
        trainingProgram2 = new TrainingProgram(2, "trainingProgram_2");
        trainingProgram3 = new TrainingProgram(3, "trainingProgram_3");

        group1 = new Group(1, "group_1", trainingProgram1);
        group2 = new Group(2, "group_2", trainingProgram2);
        group3 = new Group(3, "group_3", trainingProgram3);

        student1 = new Student(1, "student_1", "student_1", group1);
        student2 = new Student(2, "student_2", "student_2", group2);
        student3 = new Student(3, "student_3", "student_3", group3);

        teacher1 = new Teacher(1, "teacher_1", "teacher_1_lastName");
        teacher2 = new Teacher(2, "teacher_2", "teacher_2_lastName");
        teacher3 = new Teacher(3, "teacher_3", "teacher_3_lastName");

        lectureHall1 = new LectureHall(1, 1, 1, 1);
        lectureHall2 = new LectureHall(2, 2, 2, 2);
        lectureHall3 = new LectureHall(3, 3, 3, 3);

        schedule1 = new Schedule(1, LocalDate.parse("2020-01-01"), LocalTime.parse("01:01"), lectureHall1, group1, teacher1, course1);
        schedule2 = new Schedule(2, LocalDate.parse("2020-02-02"), LocalTime.parse("02:02"), lectureHall2, group2, teacher2, course2);
        schedule3 = new Schedule(3, LocalDate.parse("2020-03-03"), LocalTime.parse("03:03"), lectureHall3, group3, teacher3, course3);

        trainingProgram1.getCourses().add(course1);
        course1.getTrainingPrograms().add(trainingProgram1);
        trainingProgram2.getCourses().add(course2);
        course2.getTrainingPrograms().add(trainingProgram2);
        trainingProgram3.getCourses().add(course3);
        course3.getTrainingPrograms().add(trainingProgram3);

        teacher1.getCourses().add(course1);
        course1.getTeachers().add(teacher1);
        teacher2.getCourses().add(course2);
        course2.getTeachers().add(teacher2);
        teacher3.getCourses().add(course3);
        course3.getTeachers().add(teacher3);

        group1.getStudents().add(student1);
        group2.getStudents().add(student2);
        group3.getStudents().add(student3);

        allSchedules = List.of(schedule1, schedule2, schedule3);

        allCourses = List.of(course1, course2, course3);

        allTrainingPrograms = List.of(trainingProgram1, trainingProgram2, trainingProgram3);

        allGroups = List.of(group1, group2, group3);

        allStudents = List.of(student1, student2, student3);

        allTeachers = List.of(teacher1, teacher2, teacher3);

        allLectureHalls = List.of(lectureHall1, lectureHall2, lectureHall3);

    }

//    public void rollback() throws IOException {
//        StringBuilder sql = new StringBuilder();
//        Path path = Paths.get("src/test/resources/rollbackTestController.sql");
//        Files.lines(path).forEach(sql::append);
//
//        JdbcTemplate template = new JdbcTemplate(dataSource);
//        template.execute(sql.toString());
//    }
}
