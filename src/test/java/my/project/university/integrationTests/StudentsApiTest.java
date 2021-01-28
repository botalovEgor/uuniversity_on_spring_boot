package my.project.university.integrationTests;

import my.project.university.models.Group;
import my.project.university.models.Student;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.StudentDto;
import my.project.university.repository.StudentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentsApiTest {
    private static final String URL_PATH = "/api/students/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdShouldReturnStudentWithGivenIdFromDataBase() throws Exception {
        mvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new StudentDto(1, "student_1", "student_1", "group_1", 1)));
    }

    @Test
    void showAllShouldReturnPageWithStudent() throws Exception {
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
                .andExpect(jsonPath("$.content[1].firstName").value("student_2"))
                .andExpect(jsonPath("$.content[2].groupDescription").value("group_3"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldAddNewStudentToDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\",\n" +
                "    \"groupDescription\": \"group_2\",\n" +
                "    \"groupId\": 2\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new StudentDto(4, "name", "lastName", "group_2", 2)));

        Query query = entityManager.createQuery("select s from Student s join fetch s.group where s.id = 4");
        Student student = (Student) query.getSingleResult();

        assertEquals(4, repository.count());
        assertEquals(student, new Student(4, "name", "lastName", new Group(2, "group_2", new TrainingProgram())));
    }

    @Test
    @DirtiesContext
    void updateShouldChangeStudentWhoIsInTheRequestBody() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\",\n" +
                "    \"groupDescription\": \"group_2\",\n" +
                "    \"groupId\": 2\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new StudentDto(1, "name", "lastName", "group_2", 2)));

        Query query = entityManager.createQuery("select s from Student s join fetch s.group where s.id = 1");
        Student actual = (Student) query.getSingleResult();

        assertEquals(3, repository.count());
        assertEquals(new Student(1, "name", "lastName", new Group(2, "group_2", new TrainingProgram())), actual);
        assertEquals(actual.getGroup(), new Group(2, "group_2", new TrainingProgram()));
    }

    @Test
    @DirtiesContext
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(1));
        assertEquals(2, repository.count());
    }

    @Test
    void whenStudentNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }
}