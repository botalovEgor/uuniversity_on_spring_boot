package my.project.university.repositoty;

import my.project.university.models.Schedule;
import my.project.university.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(TestData.class)
class MyScheduleRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void getScheduleByCriteriaShouldReturnAllSchedulesWhenParametersNotSpecified() {
        Map<String, String> criteria = Map.of(
                "groupDescription", "",
                "teacherId", "",
                "from", "",
                "to", "");

        assertEquals(testData.allSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

    @Test
    void getScheduleByCriteriaShouldReturnEmptyListWhenSchedulesNotFound() {
        List<Schedule> expectedSchedules = List.of();

        Map<String, String> criteria = Map.of(
                "groupDescription", "group_1",
                "teacherId", "4",
                "from", "2021-02-06",
                "to", "");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));

    }

    @Test
    void getScheduleByCriteriaShouldReturnCorrectSchedulesForGroup() {
        List<Schedule> expectedSchedules = List.of(testData.schedule2);

        Map<String, String> criteria = Map.of(
                "groupDescription", "group_2",
                "teacherId", "",
                "from", "",
                "to", "");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

    @Test
    void getScheduleByCriteriaShouldReturnCorrectSchedulesForTeacher() {
        List<Schedule> expectedSchedules = List.of(testData.schedule3);

        Map<String, String> criteria = Map.of(
                "groupDescription", "",
                "teacherId", "3",
                "from", "",
                "to", "");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

    @Test
    void getScheduleByCriteriaShouldReturnCorrectSchedulesForTimeInterval() {
        List<Schedule> expectedSchedules = List.of(testData.schedule3);

        Map<String, String> criteria = Map.of(
                "groupDescription", "",
                "teacherId", "3",
                "from", "2020-02-02",
                "to", "2020-03-05");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

    @Test
    void getScheduleByCriteriaShouldReturnCorrectSchedulesGreatestThenTime() {
        List<Schedule> expectedSchedules = List.of(testData.schedule2, testData.schedule3);

        Map<String, String> criteria =Map.of(
                "groupDescription", "",
                "teacherId", "",
                "from", "2020-02-01",
                "to", "");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

    @Test
    void getScheduleByCriteriaShouldReturnCorrectSchedulesLessThenTime() {
        List<Schedule> expectedSchedules = List.of(testData.schedule1, testData.schedule2);

        Map<String, String> criteria =Map.of(
                "groupDescription", "",
                "teacherId", "",
                "from", "",
                "to", "2020-02-06");

        assertEquals(expectedSchedules, scheduleRepository.getScheduleByCriteria(criteria));
    }

}