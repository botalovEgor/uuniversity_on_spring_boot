package my.project.university.repositoty;

import my.project.university.models.Schedule;
import my.project.university.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(TestData.class)
class ScheduleRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfScheduleNotFound() {
        assertTrue(scheduleRepository.findById(4).isEmpty());
    }

    @Test
    void findByIdShouldReturnOptionalOfScheduleWithGivenId() {
        Schedule actual = scheduleRepository.findById(1).get();
        assertEquals(testData.schedule1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<Schedule> actual = scheduleRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allSchedules, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        Schedule adding = new Schedule(LocalDate.parse("2020-01-01"), LocalTime.parse("11:11"),
                testData.lectureHall1, testData.group2, testData.teacher3, testData.course1);
        Schedule added = scheduleRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        List<Schedule> actual = new ArrayList<>();
        scheduleRepository.findAll().forEach(actual::add);

        assertEquals(added, scheduleRepository.findById(4).get());
        assertEquals(4, actual.size());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        Schedule updating = new Schedule(1, LocalDate.parse("2022-03-06"), LocalTime.parse("12:12"),
                testData.lectureHall2, testData.group2, testData.teacher3, testData.course3);
        Schedule updated = scheduleRepository.save(updating);

        entityManager.flush();
        entityManager.clear();

        assertEquals(updated, scheduleRepository.findById(1).get());
        assertEquals(updated, updating);
    }

    @Test
    void deleteShouldDeleteScheduleWithGivenIdFromDataBase() {
        scheduleRepository.remove(1);

        entityManager.flush();
        entityManager.clear();

        List<Schedule> actual = new ArrayList<>();
        scheduleRepository.findAll().forEach(actual::add);
        assertEquals(2, actual.size());
        assertTrue(scheduleRepository.findById(1).isEmpty());
    }

}