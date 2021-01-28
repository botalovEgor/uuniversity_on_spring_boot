package my.project.university.repositoty;

import my.project.university.models.Course;
import my.project.university.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(TestData.class)
class CourseRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfCourseNotFound() {
        assertTrue(courseRepository.findById(4).isEmpty());
    }

    @Test
    void findByNameShouldReturnEmptyOptionalIfCourseNotFound() {
        assertTrue(courseRepository.findByName("ddd").isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfCourseWithGivenId() {
        Course actual = courseRepository.findById(1).get();

        assertEquals(testData.course1, actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfCourseWithGivenName() {
        Course actual = courseRepository.findByName("course_1").get();
        assertEquals(testData.course1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<Course> actual = courseRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allCourses, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        Course adding = new Course("course_4", 4);
        adding.getTrainingPrograms().add(testData.trainingProgram1);
        adding.getTeachers().add(testData.teacher1);

        Course added = courseRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        Course actual = courseRepository.findById(4).get();

        assertEquals(adding, actual);
        assertEquals(adding.getId(), actual.getId());
        assertEquals(adding.getHours(), actual.getHours());
        assertEquals(adding.getTeachers(), actual.getTeachers());
        assertEquals(adding.getTrainingPrograms(), actual.getTrainingPrograms());

        assertEquals(adding, added);
        assertEquals(adding.getId(), added.getId());
        assertEquals(adding.getHours(), added.getHours());
        assertEquals(adding.getTeachers(), added.getTeachers());
        assertEquals(adding.getTrainingPrograms(), added.getTrainingPrograms());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        Course updating = new Course(1, "course_10", 6);
        Course updated = courseRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        Course actual = courseRepository.findById(1).get();

        assertEquals(updating, actual);
        assertEquals(updating.getHours(), actual.getHours());

        assertEquals(updating, updated);
        assertEquals(updating.getHours(), updated.getHours());
    }

    @Test
    void saveShouldUpdateSetTeachersTo() {
        testData.course1.getTeachers().clear();
        testData.course1.getTeachers().add(testData.teacher2);

        Course updated = courseRepository.save(testData.course1);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated.getTeachers(), courseRepository.findById(1).get().getTeachers());
        assertEquals(testData.course1.getTeachers(), updated.getTeachers());
    }

    @Test
    void saveShouldUpdateTrainingProgramSetTo(){
        testData.course1.getTrainingPrograms().clear();
        testData.course1.getTrainingPrograms().add(testData.trainingProgram3);

        Course updated = courseRepository.save(testData.course1);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated.getTrainingPrograms(), courseRepository.findById(1).get().getTrainingPrograms());
        assertEquals(testData.course1.getTrainingPrograms(), updated.getTrainingPrograms());
    }

    @Test
    void deleteShouldDeleteCourseWithGivenIdFromDataBase() {
        courseRepository.deleteById(1);

        assertEquals(2, courseRepository.count());
        assertFalse(courseRepository.existsById(1));
    }
}