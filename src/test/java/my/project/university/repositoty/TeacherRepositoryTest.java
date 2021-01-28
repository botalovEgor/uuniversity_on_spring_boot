package my.project.university.repositoty;

import my.project.university.models.Teacher;
import my.project.university.repository.TeacherRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(TestData.class)
class TeacherRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfTeacherNotFound() {
        assertTrue(teacherRepository.findById(4).isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfTeacherWithGivenId() {
        Teacher actual = teacherRepository.findById(1).get();

        assertEquals(testData.teacher1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<Teacher> actual = teacherRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allTeachers, actual.getContent());
    }


    @Test
    void saveShouldAddNewEntityToDataBase() {
        Teacher adding = new Teacher("teacher_4", "teacher_4");
        adding.getCourses().add(testData.course1);
        Teacher added = teacherRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        Teacher actual = teacherRepository.findById(4).get();

        assertEquals(adding, actual);
        assertEquals(adding.getFirstName(), actual.getFirstName());
        assertEquals(adding.getLastName(), actual.getLastName());
        assertEquals(adding.getCourses(), actual.getCourses());

        assertEquals(adding, added);
        assertEquals(adding.getFirstName(), added.getFirstName());
        assertEquals(adding.getLastName(), added.getLastName());
        assertEquals(adding.getCourses(), added.getCourses());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        Teacher updating = new Teacher(1, "any", "changed");
        Teacher updated = teacherRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        Teacher actual = teacherRepository.findById(1).get();

        assertEquals(updating, actual);
        assertEquals(updating.getFirstName(), actual.getFirstName());
        assertEquals(updating.getLastName(), actual.getLastName());

        assertEquals(updating, updated);
        assertEquals(updating.getFirstName(), updated.getFirstName());
        assertEquals(updating.getLastName(), updated.getLastName());
    }

    @Test
    void saveShouldUpdateSetCoursesTo() {
        Teacher updating = new Teacher(1, "teacher_1", "teacher_1");
        updating.getCourses().add(testData.course2);
        Teacher updated = teacherRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated.getCourses(), teacherRepository.findById(1).get().getCourses());
    }

    @Test
    void deleteShouldDeleteTeacherWithGivenIdFromDataBase() {
        teacherRepository.deleteById(1);

        List<Teacher> actual = new ArrayList<>();
        teacherRepository.findAll().forEach(actual::add);

        assertEquals(2, actual.size());
        assertTrue(teacherRepository.findById(1).isEmpty());
    }
}