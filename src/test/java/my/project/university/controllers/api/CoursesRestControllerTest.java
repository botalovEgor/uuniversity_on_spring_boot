package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.CourseService;
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

@WebMvcTest(controllers = CoursesRestController.class)
@Import(value = CustomMappingConfiguration.class)
class CoursesRestControllerTest {
    private static final String URL_PATH = "/api/courses/";

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnCourseDtoWithGivenIdWithStatus200() throws Exception {
        CourseDto dto = new CourseDto(1, "course", 2);

        when(courseService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(courseService).findById(1);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void findByNameShouldOnlyCallFindByNameMethodAndReturnCourseDtoWithGivenNameWithStatus200() throws Exception {
        CourseDto dto = new CourseDto(1, "course", 2);

        when(courseService.findByName("course")).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "name/course")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(courseService).findByName("course");
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithCourseDtoWithStatus200() throws Exception {
        CourseDto dto = new CourseDto(1, "course", 1);
        Page<CourseDto> page = new PageImpl<>(List.of(dto), pageable, 1);

        when(courseService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("course"))
                .andExpect(jsonPath("$.content[0].hours").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(courseService).findAll(pageable);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedCourseDtoWithStatus201() throws Exception {
        CourseDto adding = new CourseDto(null, "new", 2);
        CourseDto added = new CourseDto(1, "new", 2);

        when(courseService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"name\": \"new\",\n" +
                "    \"hours\": 2\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(courseService).saveOrUpdate(adding);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedCourseDtoWithStatus200() throws Exception {
        CourseDto updating = new CourseDto(2, "changed", 2);
        when(courseService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"changed\",\n" +
                "    \"hours\": 2\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(courseService).saveOrUpdate(updating);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(courseService).delete(1);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void getTeachersShouldOnlyCallGetTeachersMethodAndReturnPageTeacherDtoWithStatus200() throws Exception {
        TeacherDto dto = new TeacherDto(1, "name", "lastName");
        Page<TeacherDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(courseService.getTeachers(1, pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH + "1/teachers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(courseService).getTeachers(1, pageable);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addTeacherShouldOnlyCallAddTeacherMethodAndReturnStatus201() throws Exception {
        mockMvc.perform(post(URL_PATH + "1/teachers/2"))
                .andExpect(status().isCreated());

        verify(courseService).addTeacher(1, 2);
        verifyNoMoreInteractions(courseService);

    }

    @Test
    void deleteTeacherShouldOnlyCallDeleteTeacherMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/teachers/2"))
                .andExpect(status().isOk());

        verify(courseService).deleteTeacher(1, 2);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void getTrainingProgramsShouldOnlyCallGetTeachersMethodAndReturnPageTrainingProgramDtoWithStatus200() throws Exception {
        TrainingProgramDto dto = new TrainingProgramDto(1, "program");
        Page<TrainingProgramDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(courseService.getTrainingPrograms(1, pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH + "1/training-programs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto))
                .andExpect(jsonPath("$.paged").value(true));

        verify(courseService).getTrainingPrograms(1, pageable);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addTrainingProgramsShouldOnlyCallAddTrainingProgramMethodAndReturnStatus201() throws Exception {
        mockMvc.perform(post(URL_PATH + "1/training-programs/2"))
                .andExpect(status().isCreated());

        verify(courseService).addTrainingProgram(1, 2);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void deleteTrainingProgramShouldOnlyCallDeleteTrainingProgramMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/training-programs/2"))
                .andExpect(status().isOk());

        verify(courseService).deleteTrainingProgram(1, 2);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(courseService).findById(4);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(courseService).findById(4);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CourseDto(null, "", -2))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CourseDto(2, "any", 2))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CourseDto(1, "", -2))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CourseDto(null, "any", 2))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void findByNameShouldConstraintViolationExceptionWhenNameIsBlank() throws Exception {
        mockMvc.perform(get(URL_PATH+"name/{name}", " "))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void getTeachersShouldReturnStatusBadRequestWhenCourseIdNotPositive() throws Exception {
        mockMvc.perform(get(URL_PATH+ "-2" + "/teachers"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(courseService);
    }

    @Test
    void addTeacherShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(post(URL_PATH+ "-2" + "/teachers/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "2" + "/teachers/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "-2" + "/teachers/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void deleteTeacherShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/teachers/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "2" + "/teachers/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/teachers/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void getTrainingProgramsShouldReturnStatusBadRequestWhenCourseIdNotPositive() throws Exception {
        mockMvc.perform(get(URL_PATH+ "-2" + "/training-programs"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(courseService);
    }

    @Test
    void addTrainingProgramShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(post(URL_PATH+ "-2" + "/training-programs/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "2" + "/training-programs/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "-2" + "/training-programs/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }

    @Test
    void deleteTrainingProgramShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/training-programs/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "2" + "/training-programs/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2" + "/training-programs/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(courseService);
    }
}