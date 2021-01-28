package my.project.university.integrationTests;

import my.project.university.models.LectureHall;
import my.project.university.repository.LectureHallRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LectureHallsApiTest {
    private static final String URL_PATH = "/api/lecture-halls/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LectureHallRepository repository;

    @Test
    void findByIdShouldReturnLectureHallWithGivenId() throws Exception {
        mvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new LectureHall(1, 1, 1, 1)));
    }

    @Test
    void showAllShouldReturnPageWithLectureHalls() throws Exception {
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
                .andExpect(jsonPath("$.content[1].housing").value(2))
                .andExpect(jsonPath("$.content[2].floor").value(3))
                .andExpect(jsonPath("$.sort[0].property").value("id"))
                .andExpect(jsonPath("$.sort[0].ascending").value(true))
                .andExpect(jsonPath("$.sort[0].descending").value(false));
    }

    @Test
    @DirtiesContext
    void addShouldAddNewLectureHallsToDataBase() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"housing\": 4,\n" +
                "    \"floor\": 4,\n" +
                "    \"number\": 4\n" +
                "}";

        mvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new LectureHall(null, 4, 4, 4)));

        assertEquals(4, repository.count());
        assertEquals(new LectureHall(4, 4, 4, 4), repository.findById(4).get());

    }

    @Test
    @DirtiesContext
    void updateShouldChangeLectureHallsWhoAreInTheRequestBody() throws Exception {
        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"housing\": 5,\n" +
                "    \"floor\": 5,\n" +
                "    \"number\": 5\n" +
                "}";

        mvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(new LectureHall(1, 5, 5, 5)));

        assertEquals(3, repository.count());
        assertEquals(new LectureHall(1, 5, 5, 5), repository.findById(1).get());
    }

    @Test
    @DirtiesContext
    void deleteShouldDeleteLectureHallWithGivenIdFromDataBase() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(1));
        assertEquals(2, repository.count());
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        mvc.perform(get(URL_PATH + "4"))
                .andExpect(status().isNotFound());
    }
}