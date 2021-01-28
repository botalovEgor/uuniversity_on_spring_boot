package my.project.university.controllers.view;

import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.TrainingProgramService;
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

@WebMvcTest(controllers = TrainingProgramsController.class)
class TrainingProgramsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingProgramService trainingProgramService;

    @Test
    void findById() throws Exception {
        TrainingProgramDto trainingProgramDto = new TrainingProgramDto();
        trainingProgramDto.setSpeciality("any");

        when(trainingProgramService.findById(1)).thenReturn(trainingProgramDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("trainingProgramDto", trainingProgramDto))
                .andExpect(view().name("trainingProgramView/trainingProgramById"));

        verify(trainingProgramService).findById(1);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void showAll() throws Exception {
        Page<TrainingProgramDto> page = new PageImpl<>(List.of(new TrainingProgramDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(trainingProgramService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("trainingProgramView/allTrainingPrograms"));

        verify(trainingProgramService).findAll(pageable);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        TrainingProgramDto adding = new TrainingProgramDto();
        adding.setSpeciality("program_4");

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/add")
                .param("speciality", "program_4"))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainingPrograms"));

        verify(trainingProgramService).saveOrUpdate(adding);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void updateWithGetHttp() throws Exception {
        TrainingProgramDto updating = new TrainingProgramDto();
        updating.setId(2);

        when(trainingProgramService.findById(2)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms/{id}/update", 2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateTrainingProgramDto", updating))
                .andExpect(view().name("trainingProgramView/update"));

        verify(trainingProgramService).findById(2);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        TrainingProgramDto updating = new TrainingProgramDto();
        updating.setId(1);
        updating.setSpeciality("changed");

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/update")
                .param("id", "1")
                .param("speciality", "changed"))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainingPrograms"));

        verify(trainingProgramService).saveOrUpdate(updating);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 3;

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms/{id}/delete", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/trainingPrograms"));

        verify(trainingProgramService).delete(id);
        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("speciality", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("speciality", "any");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("speciality", "");

        String error = "Restrictions violated:\r\n" +
                "- Speciality should not be blank\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("speciality", "any");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/trainingPrograms/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/trainingPrograms/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(trainingProgramService);
    }
}