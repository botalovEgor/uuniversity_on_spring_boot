package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.TeacherService;
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

@WebMvcTest(controllers = TeachersRestController.class)
@Import(value = CustomMappingConfiguration.class)
class TeachersRestControllerTest {
    private static final String URL_PATH = "/api/teachers/";

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TeacherService teacherService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnTeacherDtoWithGivenIdWithStatus200() throws Exception {
        TeacherDto dto = new TeacherDto(1, "name", "lastName");

        when(teacherService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(teacherService).findById(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithTeacherDtoWithStatus200() throws Exception {
        TeacherDto dto = new TeacherDto(1, "name", "lastName");
        Page<TeacherDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(teacherService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(teacherService).findAll(pageable);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedTeacherDtoWithStatus201() throws Exception {
        TeacherDto adding = new TeacherDto(null, "name", "lastName");
        TeacherDto added = new TeacherDto(1, "name", "lastName");

        when(teacherService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\"\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(teacherService).saveOrUpdate(adding);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedTeacherDtoWithStatus200() throws Exception {
        TeacherDto updating = new TeacherDto(1, "name", "lastName");

        when(teacherService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"name\",\n" +
                "    \"lastName\": \"lastName\"\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(teacherService).saveOrUpdate(updating);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(teacherService).delete(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void getCoursesShouldOnlyCallGetCoursesMethodAndReturnPageCourseDtoWithStatus200() throws Exception {
        CourseDto dto = new CourseDto(1, "course", 1);
        Page<CourseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(teacherService.getCourses(1, pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH + "1/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(teacherService).getCourses(1, pageable);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addCourseShouldOnlyCallAddCourseMethodAndReturnStatus201() throws Exception {
        mockMvc.perform(post(URL_PATH + "1/courses/2"))
                .andExpect(status().isCreated());

        verify(teacherService).addCourse(1, 2);
        verifyNoMoreInteractions(teacherService);

    }

    @Test
    void deleteCourseShouldOnlyCallDeleteCourseMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/courses/2"))
                .andExpect(status().isOk());

        verify(teacherService).deleteCourse(1, 2);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(teacherService).findById(4);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(4);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TeacherDto(null, "", ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TeacherDto(1, "name", "lastName"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TeacherDto(1, "", ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TeacherDto(null, "name", "lastName"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void getCoursesShouldReturnStatusBadRequestWhenTeacherIdNotPositive() throws Exception {
        mockMvc.perform(get(URL_PATH+ "-2" + "/courses"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(teacherService);
    }

    @Test
    void addCourseShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(post(URL_PATH+ "-2" + "/courses/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "-2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }

    @Test
    void deleteCourseShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/courses/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService);
    }
}