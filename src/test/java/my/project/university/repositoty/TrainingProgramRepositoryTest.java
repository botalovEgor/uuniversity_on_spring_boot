package my.project.university.repositoty;

import my.project.university.models.TrainingProgram;
import my.project.university.repository.TrainingProgramRepository;
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
class TrainingProgramRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfTrainingProgramNotFound() {
        assertTrue(trainingProgramRepository.findById(4).isEmpty());
    }

    @Test
    void findBySpecialityShouldReturnEmptyOptionalIfTrainingProgramNotFound() {
        assertTrue(trainingProgramRepository.findBySpeciality("ddd").isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfTrainingProgramWithGivenId() {
        TrainingProgram actual = trainingProgramRepository.findById(1).get();
        assertEquals(testData.trainingProgram1, actual);
    }

    @Test
    void findBySpecialityShouldReturnOptionalOfCourseWithGivenSpeciality() {
        TrainingProgram actual = trainingProgramRepository.findBySpeciality("trainingProgram_3").get();
        assertEquals(testData.trainingProgram3, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<TrainingProgram> actual = trainingProgramRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allTrainingPrograms, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        TrainingProgram adding = new TrainingProgram("trainingProgram_4");
        adding.getCourses().add(testData.course2);
        TrainingProgram added = trainingProgramRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        TrainingProgram actual = trainingProgramRepository.findById(4).get();

        assertEquals(adding, actual);
        assertEquals(adding.getId(), actual.getId());
        assertEquals(adding.getCourses(), actual.getCourses());

        assertEquals(adding, added);
        assertEquals(adding.getId(), added.getId());
        assertEquals(adding.getCourses(), added.getCourses());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        TrainingProgram updating = new TrainingProgram(2, "any");
        TrainingProgram updated = trainingProgramRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updating, trainingProgramRepository.findById(2).get());
        assertEquals(updating, updated);
    }

    @Test
    void saveShouldUpdateSetCoursesTo(){
        testData.trainingProgram1.getCourses().clear();
        testData.trainingProgram1.getCourses().add(testData.course2);

        TrainingProgram updated = trainingProgramRepository.save(testData.trainingProgram1);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated.getCourses(), trainingProgramRepository.findById(1).get().getCourses());
        assertEquals(testData.trainingProgram1.getCourses(), updated.getCourses());
    }

    @Test
    void deleteShouldDeleteTrainingProgramWithGivenIdFromDataBase() {
        trainingProgramRepository.deleteById(1);

        List<TrainingProgram> actual = new ArrayList<>();
        trainingProgramRepository.findAll().forEach(actual::add);

        assertEquals(2, actual.size());
        assertTrue(trainingProgramRepository.findById(1).isEmpty());
    }
}