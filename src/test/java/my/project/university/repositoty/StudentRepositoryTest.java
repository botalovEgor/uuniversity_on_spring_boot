package my.project.university.repositoty;

import my.project.university.models.Student;
import my.project.university.repository.StudentRepository;
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
class StudentRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenEntityNotFound(){
        assertTrue(studentRepository.findById(4).isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfStudentWithGivenId() {
        Student actual = studentRepository.findById(1).get();

        assertEquals(testData.student1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<Student> actual = studentRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allStudents, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        Student adding = new Student("student_4", "student_4", testData.group1);
        Student added = studentRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        Student actual = studentRepository.findById(4).get();

        assertEquals(adding, actual);
        assertEquals(adding.getFirstName(), actual.getFirstName());
        assertEquals(adding.getLastName(), actual.getLastName());
        assertEquals(adding.getGroup(), actual.getGroup());

        assertEquals(adding, added);
        assertEquals(adding.getFirstName(), added.getFirstName());
        assertEquals(adding.getLastName(), added.getLastName());
        assertEquals(adding.getGroup(), added.getGroup());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        Student updating = new Student(1, "any", "changed", testData.group2);
        Student updated = studentRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        Student actual = studentRepository.findById(1).get();

        assertEquals(updating, actual);
        assertEquals(updating.getFirstName(), actual.getFirstName());
        assertEquals(updating.getLastName(), actual.getLastName());
        assertEquals(updating.getGroup(), actual.getGroup());

        assertEquals(updating, updated);
        assertEquals(updating.getFirstName(), updated.getFirstName());
        assertEquals(updating.getLastName(), updated.getLastName());
        assertEquals(updating.getGroup(), updated.getGroup());
    }

    @Test
    void deleteShouldDeleteStudentWithGivenIdFromDataBase() {
        studentRepository.deleteById(2);

        List<Student> actual = new ArrayList<>();
        studentRepository.findAll().forEach(actual::add);

        assertEquals(2, actual.size());
        assertTrue(studentRepository.findById(2).isEmpty());
    }
}