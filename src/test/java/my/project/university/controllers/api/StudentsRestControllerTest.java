package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.StudentDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.StudentService;
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

@WebMvcTest(controllers = StudentsRestController.class)
@Import(value = CustomMappingConfiguration.class)
class StudentsRestControllerTest {
    private static final String URL_PATH = "/api/students/";

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentService studentService;


    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnStudentDtoWithGivenIdWithStatus200() throws Exception {
        StudentDto dto = new StudentDto(1, "name", "lastName", "group", 1);

        when(studentService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(studentService).findById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithStudentDtoWithStatus200() throws Exception {
        StudentDto dto = new StudentDto(1, "name", "lastName", "group", 1);
        Page<StudentDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(studentService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(studentService).findAll(pageable);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedStudentDtoWithStatus201() throws Exception {
        StudentDto adding = new StudentDto(null, "name", "lastName", "group", 1);
        StudentDto added = new StudentDto(1, "name", "lastName", "group", 1);

        when(studentService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\",\n" +
                "    \"groupDescription\": \"group\",\n" +
                "    \"groupId\": 1\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(studentService).saveOrUpdate(adding);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedStudentDtoWithStatus200() throws Exception {
        StudentDto updating = new StudentDto(1, "name", "lastName", "group", 1);

        when(studentService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\",\n" +
                "    \"groupDescription\": \"group\",\n" +
                "    \"groupId\": 1\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(studentService).saveOrUpdate(updating);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(studentService).delete(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(studentService).findById(4);

        mockMvc.perform(get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(studentService).findById(4);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new StudentDto(null, "", "", "", null))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new StudentDto(1, "name", "name", "group", 1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new StudentDto(1, "", "", "", null))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new StudentDto(null, "name", "name", "group", 1))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }
}