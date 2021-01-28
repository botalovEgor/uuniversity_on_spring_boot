package my.project.university.controllers.view;

import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.services.interfaces.CourseService;
import my.project.university.services.interfaces.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TeachersController.class)
class TeachersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Test
    void findById() throws Exception {
        TeacherDto teacherDto = new TeacherDto();

        when(teacherService.findById(2)).thenReturn(teacherDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/{id}", 2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("teacherDto", teacherDto))
                .andExpect(view().name("teacherView/oneTeacher"));

        verify(teacherService).findById(2);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void showAll() throws Exception {
        Page<TeacherDto> page = new PageImpl<>(List.of(new TeacherDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(teacherService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("teacherView/allTeachers"));

        verify(teacherService).findAll(pageable);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        TeacherDto adding = new TeacherDto();
        adding.setFirstName("teacher_4");
        adding.setLastName("teacher_4");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "teacher_4");
        form.add("lastName", "teacher_4");

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).saveOrUpdate(adding);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void updateWithGetHttp() throws Exception {
        TeacherDto updating = new TeacherDto();
        updating.setFirstName("teacher");

        when(teacherService.findById(1)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/{id}/update", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateTeacherDto", updating))
                .andExpect(view().name("teacherView/update"));

        verify(teacherService).findById(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        TeacherDto updating = new TeacherDto();
        updating.setId(1);
        updating.setFirstName("changed");
        updating.setLastName("changed");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("firstName", "changed");
        form.add("lastName", "changed");

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).saveOrUpdate(updating);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 2;

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/{id}/delete", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).delete(id);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "");
        form.add("lastName", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("firstName", "name");
        form.add("lastName", "name");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("firstName", "");
        form.add("lastName", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("firstName", "name");
        form.add("lastName", "name");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void addCourseToTeacherWithPostHttpShouldConstraintViolationExceptionWhenParametersIsInvalid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers/addCourse")
                .param("teacherId", "-2")
                .param("courseName", ""))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);

    }

    @Test
    void getAllTeacherInCourseShouldConstraintViolationExceptionWhenParameterIsInvalid() throws Exception {

        String error = "- Course id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/byCourse")
                .param("courseId", "-2"))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(teacherService);
    }
}