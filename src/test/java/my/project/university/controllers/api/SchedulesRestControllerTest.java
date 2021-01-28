package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SchedulesRestController.class)
@Import(value = CustomMappingConfiguration.class)
class SchedulesRestControllerTest {
    private static final String URL_PATH = "/api/schedules/";

    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ScheduleService scheduleService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnScheduleDtoWithGivenIdWithStatus200() throws Exception {
        ScheduleDto dto = new ScheduleDto(1, "2020-01-01", "01:01:00", 1, 1, 1, 1,
                "group", 1, "name", "lastName", "course");

        when(scheduleService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(dto));

        verify(scheduleService).findById(1);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithScheduleDtoWithStatus200() throws Exception {
        ScheduleDto dto = new ScheduleDto(1, "2020-01-01", "01:01:00", 1, 1, 1, 1,
                "group", 1, "name", "lastName", "course");
        Page<ScheduleDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(scheduleService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(scheduleService).findAll(pageable);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedScheduleDtoWithStatus201() throws Exception {
        ScheduleDto adding = new ScheduleDto(null, "2030-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        ScheduleDto added = new ScheduleDto(1, "2030-01-01", "01:01:00", 1, 1, 1, 1,
                "group", 1, "name", "lastName", "course");

        when(scheduleService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"lessonDate\": \"2030-01-01\",\n" +
                "    \"lessonTime\": \"01:01:00\",\n" +
                "    \"lectureHallId\": 1,\n" +
                "    \"lectureHallHousing\": null,\n" +
                "    \"lectureHallFloor\": null,\n" +
                "    \"lectureHallNumber\": null,\n" +
                "    \"groupDescription\": \"group\",\n" +
                "    \"teacherId\": 1,\n" +
                "    \"teacherFirstName\": null,\n" +
                "    \"teacherLastName\": null,\n" +
                "    \"courseName\": \"course\"\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(scheduleService).saveOrUpdate(adding);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedScheduleDtoWithStatus200() throws Exception {
        ScheduleDto updating = new ScheduleDto(1, "2030-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        ScheduleDto updated = new ScheduleDto(1, "2030-01-01", "01:01:00", 1, 1, 1, 1,
                "group", 1, "name", "lastName", "course");
        when(scheduleService.saveOrUpdate(updating)).thenReturn(updated);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"lessonDate\": \"2030-01-01\",\n" +
                "    \"lessonTime\": \"01:01:00\",\n" +
                "    \"lectureHallId\": 1,\n" +
                "    \"groupDescription\": \"group\",\n" +
                "    \"teacherId\": 1,\n" +
                "    \"courseName\": \"course\"\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updated));

        verify(scheduleService).saveOrUpdate(updating);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(scheduleService).delete(1);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void getScheduleByCriteriaShouldOnlyCallgetScheduleByCriteriaWithUrlParametersAmdReturnListScheduleDtoWithStatus200() throws Exception {
        List<ScheduleDto> scheduleDtos = List.of(new ScheduleDto());

        MultiValueMap<String, String> paramsForm = new LinkedMultiValueMap<>();
        paramsForm.add("groupDescription", "group");
        paramsForm.add("teacherId", "");
        paramsForm.add("from", "");
        paramsForm.add("to", "");

        Map<String, String> filters = new HashMap<>();
        filters.put("groupDescription", "group");
        filters.put("teacherId", "");
        filters.put("from", "");
        filters.put("to", "");

        when(scheduleService.getScheduleByCriteria(filters)).thenReturn(scheduleDtos);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "filter")
                .params(paramsForm))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]").value(scheduleDtos.get(0)));

        verify(scheduleService).getScheduleByCriteria(filters);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(scheduleService).findById(4);

        mockMvc.perform(get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(scheduleService).findById(4);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        ScheduleDto dto = new ScheduleDto(null, "2020-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        ScheduleDto dto = new ScheduleDto(1, "2030-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        ScheduleDto dto = new ScheduleDto(1, "2020-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        ScheduleDto dto = new ScheduleDto(null, "2030-01-01", "01:01:00", 1, null, null, null,
                "group", 1, null, null, "course");
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(scheduleService);
    }
}