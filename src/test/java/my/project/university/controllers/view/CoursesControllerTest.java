package my.project.university.controllers.view;

import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.CourseService;
import my.project.university.services.interfaces.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CoursesController.class)
class CoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnViewWithCourseDtoWithGivenId() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(1);

        when(courseService.findById(1)).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("courseDto", courseDto))
                .andExpect(view().name("courseView/singleCourse"));

        verify(courseService).findById(1);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void whenEntityNotFoundByIdShouldOpenErrorPage() throws Exception {
        doThrow(NotFoundEntityException.class).when(courseService).findById(4);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", 4))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(view().name("exceptionView/error"));

        verify(courseService).findById(4);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void findByNameShouldOnlyCallFindByNameMethodAndReturnViewWithCourseDtoWithGivenName() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("any");

        when(courseService.findByName("any")).thenReturn(courseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/byName")
                .param("name", "any"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("courseDto", courseDto))
                .andExpect(view().name("courseView/singleCourse"));

        verify(courseService).findByName("any");
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnViewWithPageWithCourseDto() throws Exception {
        Page<CourseDto> page = new PageImpl<>(List.of(new CourseDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(courseService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("courseView/allCourses"));

        verify(courseService).findAll(pageable);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndRedirectToHTMLWithCourses() throws Exception {
        CourseDto adding = new CourseDto();
        adding.setName("course_4");
        adding.setHours(4);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "course_4");
        form.add("hours", "4");

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService).saveOrUpdate(adding);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateWithGetHttpShouldPutToModelUpdatingCourseAndReturnViewWithUpdateForm() throws Exception {
        CourseDto updating = new CourseDto();
        updating.setId(1);
        updating.setName("any");

        when(courseService.findById(1)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}/update", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateCourseDto", updating))
                .andExpect(view().name("courseView/update"));

        verify(courseService).findById(1);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndRedirectToViewWithCourses() throws Exception {
        CourseDto updating = new CourseDto();
        updating.setId(2);
        updating.setName("changed");
        updating.setHours(5);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "2");
        form.add("name", "changed");
        form.add("hours", "5");

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService).saveOrUpdate(updating);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndRedirectToViewWithCourses() throws Exception {
        int id = 3;

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/courses"));

        verify(courseService).delete(id);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "");
        form.add("hours", "-2");

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "2");
        form.add("name", "any");
        form.add("hours", "2");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("name", "");
        form.add("hours", "-2");

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("name", "any");
        form.add("hours", "2");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void findByNameShouldConstraintViolationExceptionWhenNameIsBlank() throws Exception {

        String error = "- Course name should not be blank\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/byName")
                .param("name", ""))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(courseService);
    }
}