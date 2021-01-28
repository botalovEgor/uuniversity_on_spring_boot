package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.LectureHall;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.LectureHallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LectureHallsRestController.class)
@Import(value = CustomMappingConfiguration.class)
class LectureHallsRestControllerTest {
    private static final String URL_PATH = "/api/lecture-halls/";
    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureHallService lectureHallService;


    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnLectureHallWithGivenIdWithStatus200() throws Exception {
        LectureHall lectureHall = new LectureHall(1, 1, 1, 1);

        when(lectureHallService.findById(1)).thenReturn(lectureHall);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(lectureHall));

        verify(lectureHallService).findById(1);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithLectureHallWithStatus200() throws Exception {
        LectureHall lectureHall = new LectureHall(1, 1, 1, 1);
        Page<LectureHall> page = new PageImpl<>(List.of(lectureHall), pageable, 1);
        when(lectureHallService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(lectureHall));

        verify(lectureHallService).findAll(pageable);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedLectureHallWithStatus201() throws Exception {
        LectureHall adding = new LectureHall(null, 1, 1, 1);
        LectureHall added = new LectureHall(1, 1, 1, 1);

        when(lectureHallService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"housing\": 1,\n" +
                "    \"floor\": 1,\n" +
                "    \"number\": 1\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(lectureHallService).saveOrUpdate(adding);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedLectureHallWithStatus200() throws Exception {
        LectureHall updating = new LectureHall(1, 1, 1, 1);
        when(lectureHallService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"housing\": 1,\n" +
                "    \"floor\": 1,\n" +
                "    \"number\": 1\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(lectureHallService).saveOrUpdate(updating);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(lectureHallService).delete(1);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(lectureHallService).findById(4);

        mockMvc.perform(get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(lectureHallService).findById(4);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LectureHall(null, -1, -2, -1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LectureHall(1, 1, 2, 1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LectureHall(1, -1, -2, -1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LectureHall(null, 1, 2, 1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lectureHallService);
    }
}