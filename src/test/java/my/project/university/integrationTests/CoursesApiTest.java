package my.project.university.integrationTests;

import my.project.university.models.Course;
import my.project.university.models.dto.CourseDto;
import my.project.university.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CoursesApiTest {
    private static final String URL_PATH = "/api/courses/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdShouldReturnCourseDtoWithGivenId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new CourseDto(1, "course_1", 1)));
    }

    @Test
    void findByNameShouldReturnCourseDtoWithGivenName() throws Exception {
        mvc.perform(get(URL_PATH + "name/course_2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new CourseDto(2, "course_2", 2)));
    }

    @Test
    void showAllShouldReturnPageWithContentTypedCourseAndCorrectOtherParameters() throws Exception {
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
                .andExpect(jsonPath("$.content[0].name").value("course_1"))
                .andExpect(jsonPath("$.content[1].hours").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.sort[0].property").value("id"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldSaveGivenEntityToDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"name\": \"new\",\n" +
                "    \"hours\": 2\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("new"))
                .andExpect(jsonPath("$.hours").value(2));

        assertEquals(4, repository.count());
        assertEquals(new Course(4, "new", 2), repository.findById(4).get());
    }

    @Test
    @DirtiesContext
    void updateShouldChangeEntityStateInDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"changed\",\n" +
                "    \"hours\": 5\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("changed"))
                .andExpect(jsonPath("$.hours").value(5));

        assertEquals(3, repository.count());
        assertEquals(new Course(2, "changed", 5), repository.findById(2).get());
    }

    @Test
    @DirtiesContext
    void deleteShouldReturnDeleteEntityFromDataBase() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertEquals(2, repository.count());
        assertFalse(repository.existsById(1));
    }


    @Test
    void getTeachersShouldReturnPageTeachersWhoTeachCourseWithGivenId() throws Exception {
        mvc.perform(get(URL_PATH + "1/teachers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("teacher_1"))
                .andExpect(jsonPath("$.content[0].lastName").value("teacher_1_lastName"))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void addTeacherShouldEnrollTeacherWithGivenIdOnCourseWithGivenId() throws Exception {
        mvc.perform(post(URL_PATH + "1/teachers/2"))
                .andExpect(status().isCreated());

        Query query = entityManager.createNativeQuery("select teacher_id from course_teacher where course_id = 1");
        List result = query.getResultList();

        assertEquals(2, result.size());
        assertTrue(result.contains(2));
    }

    @Test
    @DirtiesContext
    void deleteTeacherShouldDeleteTeacherWithGivenIdFromListTeachersCourseWithGivenId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/teachers/1"))
                .andExpect(status().isOk());

        Query query = entityManager.createNativeQuery("select teacher_id from course_teacher where course_id = 1");
        List result = query.getResultList();

        assertTrue(result.isEmpty());
    }

    @Test
    void getTrainingProgramsShouldReturnListTrainingProgramsInWhichCourseWithGivenIdAreTaught() throws Exception {
        mvc.perform(get(URL_PATH + "1/training-programs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].speciality").value("trainingProgram_1"))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void addTrainingProgramsShouldAddTrainingProgramWithGivenIdToCourseWithGivenId() throws Exception {
        mvc.perform(post(URL_PATH + "1/training-programs/2"))
                .andExpect(status().isCreated());

        Query query = entityManager.createNativeQuery("select trainingprogram_id from course_trainingprogram where course_id = 1");
        List result = query.getResultList();

        assertEquals(2, result.size());
        assertTrue(result.contains(2));
    }

    @Test
    @DirtiesContext
    void deleteTrainingProgramShouldDeleteTrainingProgramWithGivenIdFromCourseWithGivenId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/training-programs/1"))
                .andExpect(status().isOk());

        Query query = entityManager.createNativeQuery("select trainingprogram_id from course_trainingprogram where course_id = 1");
        List result = query.getResultList();

        assertTrue(result.isEmpty());
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenEntityNotFoundByNameShouldReturnStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "/name/unknown"))
                .andExpect(status().isNotFound());
    }
}