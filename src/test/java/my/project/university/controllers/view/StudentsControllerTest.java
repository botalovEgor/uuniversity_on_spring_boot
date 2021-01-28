package my.project.university.controllers.view;

import my.project.university.models.dto.StudentDto;
import my.project.university.services.interfaces.StudentService;
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

@WebMvcTest(controllers = StudentsController.class)
class StudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void findById() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("student");

        when(studentService.findById(1)).thenReturn(studentDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("studentDto", studentDto))
                .andExpect(view().name("studentView/oneStudent"));

        verify(studentService).findById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void showAll() throws Exception {
        Page<StudentDto> page = new PageImpl<>(List.of(new StudentDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(studentService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("studentView/allStudents"));

        verify(studentService).findAll(pageable);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        StudentDto adding = new StudentDto();
        adding.setFirstName("student_4");
        adding.setLastName("student_4");
        adding.setGroupDescription("group_1");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "student_4");
        form.add("lastName", "student_4");
        form.add("groupDescription", "group_1");

        mockMvc.perform(MockMvcRequestBuilders.post("/students/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).saveOrUpdate(adding);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateWithGetHttp() throws Exception {
        StudentDto updating = new StudentDto();

        when(studentService.findById(2)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}/update", 2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateStudentDto", updating))
                .andExpect(view().name("studentView/update"));

        verify(studentService).findById(2);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        StudentDto updating = new StudentDto();
        updating.setId(2);
        updating.setFirstName("changed");
        updating.setLastName("changed");
        updating.setGroupDescription("group_3");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "2");
        form.add("firstName", "changed");
        form.add("lastName", "changed");
        form.add("groupDescription", "group_3");

        mockMvc.perform(MockMvcRequestBuilders.post("/students/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).saveOrUpdate(updating);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}/delete", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/students"));

        verify(studentService).delete(id);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "");
        form.add("lastName", "");
        form.add("groupDescription", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/students/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("firstName", "name");
        form.add("lastName", "name");
        form.add("groupDescription", "any");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/students/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("firstName", "");
        form.add("lastName", "");
        form.add("groupDescription", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/students/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("firstName", "name");
        form.add("lastName", "name");
        form.add("groupDescription", "any");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/students/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void getStudentInGroupShouldConstraintViolationExceptionWhenGroupIdNotPositive() throws Exception {

        String error = "- Croup id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/students/getStudentInGroup")
                .param("groupId", "-2"))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(studentService);
    }
}