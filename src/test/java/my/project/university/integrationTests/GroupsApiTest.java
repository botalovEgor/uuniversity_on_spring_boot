package my.project.university.integrationTests;

import my.project.university.models.Group;
import my.project.university.models.Student;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.GroupDto;
import my.project.university.models.dto.StudentDto;
import my.project.university.repository.GroupRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GroupsApiTest {
    private static final String URL_PATH = "/api/groups/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GroupRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdShouldReturnGroupWIthGivenIdFromDataBase() throws Exception {
        mvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new GroupDto(1, "group_1", 1, "trainingProgram_1")));
    }

    @Test
    void findByDescriptionShouldReturnGroupWIthGivenDescriptionFromDataBase() throws Exception {
        mvc.perform(get(URL_PATH + "description/group_1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new GroupDto(1, "group_1", 1, "trainingProgram_1")));
    }

    @Test
    void showAllShouldReturnPageWithGroups() throws Exception {
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
                .andExpect(jsonPath("$.content[0].description").value("group_1"))
                .andExpect(jsonPath("$.content[1].trainingProgramId").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.sort[0].property").value("id"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldAddNewGroupToDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"description\": \"new\",\n" +
                "    \"trainingProgramId\": 1,\n" +
                "    \"trainingProgram\": \"trainingProgram_1\"\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new GroupDto(4, "new", 1, "trainingProgram_1")));

        Query query = entityManager.createQuery("select g from Group g join fetch g.trainingProgram t where g.id = 4", Group.class);
        Group actual = (Group) query.getSingleResult();

        Group expected = new Group(4, "new", new TrainingProgram(1, "trainingProgram_1"));

        assertEquals(4, repository.count());
        assertEquals(expected, actual);
        assertEquals(expected.getTrainingProgram(), actual.getTrainingProgram());
    }

    @Test
    @DirtiesContext
    void updateShouldChangeDataInGroup() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"description\": \"changed\",\n" +
                "    \"trainingProgramId\": 2,\n" +
                "    \"trainingProgram\": \"trainingProgram_2\"\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new GroupDto(1, "changed", 2, "trainingProgram_2")));

        Query query = entityManager.createQuery("select g from Group g join fetch g.trainingProgram t where g.id = 1", Group.class);
        Group actual = (Group) query.getSingleResult();

        Group expected = new Group(1, "changed", new TrainingProgram(2, "trainingProgram_2"));

        assertEquals(expected, actual);
        assertEquals(expected.getTrainingProgram(), actual.getTrainingProgram());
        assertEquals(3, repository.count());
    }

    @Test
    @DirtiesContext
    void deleteShouldDeleteGroupWithGivenIdFromDataBase() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(1));
        assertEquals(2, repository.count());
    }

    @Test
    void getStudentsShouldReturnPageWithStudentsWhoAreInTheGroupWithGivenId() throws Exception {
        mvc.perform(get(URL_PATH + "1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0]").value(new StudentDto(1, "student_1", "student_1", "group_1", 1)))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void addStudentShouldAddStudentWithGivenIdToGroupsWithGivenId() throws Exception {
        mvc.perform(post(URL_PATH + "1/students/2"))
                .andExpect(status().isCreated());

        Query query = entityManager.createQuery("select g from Group g join fetch g.students t where g.id = 1", Group.class);
        Group actual = (Group) query.getSingleResult();
        Set<Student> students = actual.getStudents();

        assertEquals(2, students.size());
        assertTrue(students.contains(new Student(2)));
    }

    @Test
    void whenGroupNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGroupNotFoundByDescriptionShouldReturnStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }
}