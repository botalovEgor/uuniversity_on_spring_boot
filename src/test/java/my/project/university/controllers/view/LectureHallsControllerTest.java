package my.project.university.controllers.view;

import my.project.university.models.LectureHall;
import my.project.university.services.interfaces.LectureHallService;
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

@WebMvcTest(controllers = LectureHallsController.class)
class LectureHallsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureHallService lectureHallService;

    @Test
    void findById() throws Exception {
        LectureHall lectureHall = new LectureHall(1, 1, 1, 1);

        when(lectureHallService.findById(1)).thenReturn(lectureHall);

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("lectureHall", lectureHall))
                .andExpect(view().name("lectureHallView/oneLectureHall"));

        verify(lectureHallService).findById(1);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void showAll() throws Exception {
        Page<LectureHall> page = new PageImpl<>(List.of(new LectureHall()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(lectureHallService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("lectureHallView/allLectureHalls"));

        verify(lectureHallService).findAll(pageable);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        LectureHall adding = new LectureHall(null, 4, 4, 4);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("housing", "4");
        form.add("floor", "4");
        form.add("number", "4");

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectureHalls"));

        verify(lectureHallService).saveOrUpdate(adding);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void updateWithGetHttp() throws Exception {
        LectureHall updating = new LectureHall(1, 1, 1, 1);

        when(lectureHallService.findById(1)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls/{id}/update", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateLectureHall", updating))
                .andExpect(view().name("lectureHallView/update"));

        verify(lectureHallService).findById(1);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        LectureHall updating = new LectureHall(1, 2, 5, 7);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("housing", "2");
        form.add("floor", "5");
        form.add("number", "7");

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectureHalls"));

        verify(lectureHallService).saveOrUpdate(updating);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 3;

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/lectureHalls"));

        verify(lectureHallService).delete(id);
        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenParameterAreInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("housing", "-1");
        form.add("floor", "");
        form.add("number", "-2");

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "4");
        form.add("housing", "1");
        form.add("floor", "5");
        form.add("number", "2");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenParametersAreInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("housing", "-2");
        form.add("floor", "");
        form.add("number", "-1");

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("housing", "2");
        form.add("floor", "4");
        form.add("number", "1");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/lectureHalls/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/lectureHalls/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(lectureHallService);
    }
}