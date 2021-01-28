package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.TrainingProgramService;
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


@WebMvcTest(controllers = TrainingProgramsRestController.class)
@Import(value = CustomMappingConfiguration.class)
class TrainingProgramsRestControllerTest {
    private static final String URL_PATH = "/api/training-programs/";

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainingProgramService trainingProgramService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnTrainingProgramDtoWithGivenIdWithStatus200() throws Exception {
        TrainingProgramDto dto = new TrainingProgramDto(1, "speciality");

        when(trainingProgramService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(trainingProgramService).findById(1);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void findBySpecialityShouldOnlyCallFindBySpecialityMethodAndReturnCourseDtoWithGivenNameWithStatus200() throws Exception {
        TrainingProgramDto dto = new TrainingProgramDto(1, "speciality");

        when(trainingProgramService.findBySpeciality("speciality")).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "speciality/speciality")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(trainingProgramService).findBySpeciality("speciality");
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithTrainingProgramDtoWithStatus200() throws Exception {
        TrainingProgramDto dto = new TrainingProgramDto(1, "speciality");
        Page<TrainingProgramDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(trainingProgramService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(trainingProgramService).findAll(pageable);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedTrainingProgramDtoWithStatus201() throws Exception {
        TrainingProgramDto adding = new TrainingProgramDto(null, "speciality");
        TrainingProgramDto added = new TrainingProgramDto(1, "speciality");

        when(trainingProgramService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"speciality\": \"speciality\"\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(trainingProgramService).saveOrUpdate(adding);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedTrainingProgramDtoWithStatus200() throws Exception {
        TrainingProgramDto updating = new TrainingProgramDto(1, "speciality");

        when(trainingProgramService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"speciality\": \"speciality\"\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(trainingProgramService).saveOrUpdate(updating);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(trainingProgramService).delete(1);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void getCoursesShouldOnlyCallGetCoursesMethodAndReturnPageCourseDtoWithStatus200() throws Exception {
        CourseDto dto = new CourseDto(1, "course", 1);
        Page<CourseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(trainingProgramService.getCourses(1, pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH + "1/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(trainingProgramService).getCourses(1, pageable);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addCourseShouldOnlyCallAddCourseMethodAndReturnStatus201() throws Exception {
        mockMvc.perform(post(URL_PATH + "1/courses/2"))
                .andExpect(status().isCreated());

        verify(trainingProgramService).addCourse(1, 2);
        verifyNoMoreInteractions(trainingProgramService);

    }

    @Test
    void deleteCourseShouldOnlyCallDeleteCourseMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1/courses/2"))
                .andExpect(status().isOk());

        verify(trainingProgramService).deleteCourse(1, 2);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(trainingProgramService).findById(4);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "4"))
                .andExpect(status().isNotFound());

        verify(trainingProgramService).findById(4);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TrainingProgramDto(null, ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TrainingProgramDto(1, "speciality"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TrainingProgramDto(1, ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TrainingProgramDto(null, "speciality"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void findBySpecialityShouldConstraintViolationExceptionWhenNameIsBlank() throws Exception {
        mockMvc.perform(get(URL_PATH + "speciality/{speciality}", " "))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void getCoursesShouldReturnStatusBadRequestWhenCourseIdNotPositive() throws Exception {
        mockMvc.perform(get(URL_PATH + "-2" + "/courses"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void addCourseShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(post(URL_PATH + "-2" + "/courses/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH + "2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH + "-2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }

    @Test
    void deleteCourseShouldReturnStatusBadRequestWhenCourseIdAndOrTeacherIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "-2" + "/courses/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "-2" + "/courses/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainingProgramService);
    }
}