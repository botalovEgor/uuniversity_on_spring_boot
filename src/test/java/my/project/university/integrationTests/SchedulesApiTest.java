package my.project.university.integrationTests;

import my.project.university.models.Schedule;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SchedulesApiTest {
    private static final String URL_PATH = "/api/schedules/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ScheduleRepository repository;

    @Test
    void findByIdShouldReturnScheduleElementWithGivenId() throws Exception {
        mvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new ScheduleDto(1, "2020-01-01", "01:01:00", 1, 1, 1, 1,
                        "group_1", 1, "teacher_1", "teacher_1_lastName", "course_1")));
    }

    @Test
    void showAllShouldReturnPageWithScheduleElementFromDataBase() throws Exception {
        mvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.unPaged").value(false))
                .andExpect(jsonPath("$.emptyPage").value(false))
                .andExpect(jsonPath("$.lastPage").value(true))
                .andExpect(jsonPath("$.firstPage").value(true))
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].lessonDate").value("2020-02-02"))
                .andExpect(jsonPath("$.content[2].lessonTime").value("03:03:00"))
                .andExpect(jsonPath("$.content[0].lectureHallId").value(1))
                .andExpect(jsonPath("$.content[1].groupDescription").value("group_2"))
                .andExpect(jsonPath("$.content[2].teacherId").value(3))
                .andExpect(jsonPath("$.content[2].courseName").value("course_3"))
                .andExpect(jsonPath("$.sort[0].property").value("id"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldAddNewScheduleElementToDataBase() throws Exception {
        ScheduleDto added = new ScheduleDto(4, "2030-01-01", "01:01:00", 1, 1, 1, 1,
                "group_1", 1, "teacher_1", "teacher_1_lastName", "course_2");

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"lessonDate\": \"2030-01-01\",\n" +
                "    \"lessonTime\": \"01:01:00\",\n" +
                "    \"lectureHallId\": 1,\n" +
                "    \"lectureHallHousing\": null,\n" +
                "    \"lectureHallFloor\": null,\n" +
                "    \"lectureHallNumber\": null,\n" +
                "    \"groupDescription\": \"group_1\",\n" +
                "    \"teacherId\": 1,\n" +
                "    \"teacherFirstName\": null,\n" +
                "    \"teacherLastName\": null,\n" +
                "    \"courseName\": \"course_2\"\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        Schedule actual = repository.findById(4).get();

        assertEquals(4, repository.count());
        assertEquals(LocalDate.parse("2030-01-01"), actual.getLessonDate());
        assertEquals(LocalTime.parse("01:01:00"), actual.getLessonTime());
        assertEquals(1, actual.getLectureHall().getId());
        assertEquals(1, actual.getTeacher().getId());
        assertEquals("group_1", actual.getGroup().getDescription());
        assertEquals("course_2", actual.getCourse().getName());
    }

    @Test
    @DirtiesContext
    void updateShouldChangeScheduleElementWhoAreInTheRequestBody() throws Exception {
        ScheduleDto updated = new ScheduleDto(1, "2030-01-01", "05:05:00", 2, 2, 2, 2,
                "group_2", 3, "teacher_3", "teacher_3_lastName", "course_2");

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"lessonDate\": \"2030-01-01\",\n" +
                "    \"lessonTime\": \"05:05:00\",\n" +
                "    \"lectureHallId\": 2,\n" +
                "    \"groupDescription\": \"group_2\",\n" +
                "    \"teacherId\": 3,\n" +
                "    \"courseName\": \"course_2\"\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updated));

        Schedule actual = repository.findById(1).get();

        assertEquals(3, repository.count());
        assertEquals(LocalDate.parse("2030-01-01"), actual.getLessonDate());
        assertEquals(LocalTime.parse("05:05:00"), actual.getLessonTime());
        assertEquals(2, actual.getLectureHall().getId());
        assertEquals(3, actual.getTeacher().getId());
        assertEquals("group_2", actual.getGroup().getDescription());
        assertEquals("course_2", actual.getCourse().getName());
    }

    @Test
    @DirtiesContext
    void deleteShouldDeleteScheduleElementFromDataBase() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(1));
        assertEquals(2, repository.count());
    }

    @Test
    void getScheduleByCriteriaShouldReturnScheduleElementThatMatchTheSpecifiedFilters() throws Exception {
        MultiValueMap<String, String> paramsForm = new LinkedMultiValueMap<>();
        paramsForm.add("groupDescription", "group_1");
        paramsForm.add("teacherId", "");
        paramsForm.add("from", "");
        paramsForm.add("to", "");

        Map<String, String> filters = new HashMap<>();
        filters.put("groupDescription", "group_1");
        filters.put("teacherId", "");
        filters.put("from", "");
        filters.put("to", "");

        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "filter")
                .params(paramsForm))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }
}