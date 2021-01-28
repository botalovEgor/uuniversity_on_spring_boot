package my.project.university.integrationTests;

import my.project.university.models.Teacher;
import my.project.university.models.dto.TeacherDto;
import my.project.university.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeachersApiTest {
    private static final String URL_PATH = "/api/teachers/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeacherRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdShouldReturnTeacherWithGivenId() throws Exception {
        mvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new TeacherDto(1, "teacher_1", "teacher_1_lastName")));
    }

    @Test
    void showAllShouldReturnPageWithTeachers() throws Exception {
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
                .andExpect(jsonPath("$.content[1].firstName").value("teacher_2"))
                .andExpect(jsonPath("$.content[2].lastName").value("teacher_3_lastName"))
                .andExpect(jsonPath("$.sort[0].property").value("id"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldNewTeacherToDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\"\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new TeacherDto(4, "name", "lastName")));

        assertEquals(4, repository.count());
        assertEquals(new Teacher(4, "name", "lastName"), repository.findById(4).get());
    }

    @Test
    @DirtiesContext
    void updateShouldChangeTeacherWhoIsInRequestBody() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\"\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new TeacherDto(1, "name", "lastName")));

        assertEquals(3, repository.count());
        assertEquals(new Teacher(1, "name", "lastName"), repository.findById(1).get());
    }

    @Test
    @DirtiesContext
    void deleteShouldDeleteTeacherWithGivenIdFromDataBAse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(1));
        assertEquals(2, repository.count());
    }

    @Test
    void getCoursesShouldReturnPageWithCourseWhichTeacherWithGivenIdAreTaught() throws Exception {
        mvc.perform(get(URL_PATH + "1/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("course_1"))
                .andExpect(jsonPath("$.content[0].hours").value(1))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void addCourseShouldAddCourseToListOfTeacherCourses() throws Exception {
        mvc.perform(post(URL_PATH + "1/courses/2"))
                .andExpect(status().isCreated());

        Query query = entityManager.createNativeQuery("select course_id from course_teacher where teacher_id = 1");
        List result = query.getResultList();

        assertEquals(2, result.size());
        assertTrue(result.contains(2));
    }

    @Test
    @DirtiesContext
    void deleteCourse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/courses/1"))
                .andExpect(status().isOk());

        Query query = entityManager.createNativeQuery("select course_id from course_teacher where teacher_id = 1");
        List result = query.getResultList();

        assertTrue(result.isEmpty());
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }
}