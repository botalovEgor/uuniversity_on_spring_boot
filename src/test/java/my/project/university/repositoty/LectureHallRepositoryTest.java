package my.project.university.repositoty;


import my.project.university.models.LectureHall;
import my.project.university.repository.LectureHallRepository;
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
class LectureHallRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LectureHallRepository lectureHallRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfLectureHallNotFound(){
        assertTrue(lectureHallRepository.findById(4).isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfLectureHallWithGivenId() {
        LectureHall actual = lectureHallRepository.findById(1).get();
        assertEquals(testData.lectureHall1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<LectureHall> actual = lectureHallRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allLectureHalls, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        LectureHall adding = new LectureHall(null, 4, 4, 4);
        LectureHall added = lectureHallRepository.save(adding);

        List<LectureHall> lectureHalls = new ArrayList<>();
        lectureHallRepository.findAll().forEach(lectureHalls::add);

        LectureHall actual = lectureHallRepository.findById(4).get();

        assertEquals(4, lectureHalls.size());

        assertEquals(adding, actual);

        assertEquals(adding, added);
        assertEquals(added.getId(), actual.getId());

    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        LectureHall updating = new LectureHall(1, 1, 2, 3);
        LectureHall updated = lectureHallRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated, lectureHallRepository.findById(1).get());
        assertEquals(updated, updating);
    }

    @Test
    void deleteShouldDeleteLectureHallWithGivenIdFromDataBase() {
        lectureHallRepository.deleteById(1);

        List<LectureHall> actual = new ArrayList<>();
        lectureHallRepository.findAll().forEach(actual::add);

        assertEquals(2, actual.size());
        assertTrue(lectureHallRepository.findById(1).isEmpty());
    }
}