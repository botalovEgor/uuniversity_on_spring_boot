package my.project.university.controllers.view;

import my.project.university.models.dto.ScheduleDto;
import my.project.university.services.interfaces.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SchedulesController.class)
class SchedulesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void findById() throws Exception {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setCourseName("course");

        when(scheduleService.findById(1)).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("scheduleDto", scheduleDto))
                .andExpect(view().name("scheduleView/oneSchedule"));

        verify(scheduleService).findById(1);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void showAll() throws Exception {
        Page<ScheduleDto> page = new PageImpl<>(List.of(new ScheduleDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(scheduleService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("scheduleView/allSchedules"));

        verify(scheduleService).findAll(pageable);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        ScheduleDto adding = new ScheduleDto();
        adding.setLessonDate("2040-05-10");
        adding.setLessonTime("21:16:00");
        adding.setLectureHallId(1);
        adding.setGroupDescription("group_1");
        adding.setTeacherId(1);
        adding.setCourseName("course_1");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("lessonDate", "2040-05-10");
        form.add("lessonTime", "21:16:00");
        form.add("lectureHallId", "1");
        form.add("groupDescription", "group_1");
        form.add("teacherId", "1");
        form.add("courseName", "course_1");

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedules"));

        verify(scheduleService).saveOrUpdate(adding);
        verifyNoMoreInteractions(scheduleService);

    }

    @Test
    void updateWithGetHttp() throws Exception {
        ScheduleDto updating = new ScheduleDto();
        updating.setCourseName("course");

        when(scheduleService.findById(1)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}/update", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("updateScheduleDto", updating))
                .andExpect(view().name("scheduleView/update"));

        verify(scheduleService).findById(1);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        ScheduleDto updating = new ScheduleDto();
        updating.setId(1);
        updating.setLessonDate("2040-10-10");
        updating.setLessonTime("21:10:00");
        updating.setLectureHallId(2);
        updating.setGroupDescription("group_2");
        updating.setTeacherId(2);
        updating.setCourseName("course_2");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("lessonDate", "2040-10-10");
        form.add("lessonTime", "21:10:00");
        form.add("lectureHallId", "2");
        form.add("groupDescription", "group_2");
        form.add("teacherId", "2");
        form.add("courseName", "course_2");

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedules"));

        verify(scheduleService).saveOrUpdate(updating);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 3;

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}/delete", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/schedules"));

        verify(scheduleService).delete(id);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void getScheduleByCriteriaWithPostHttp() throws Exception {
        List<ScheduleDto> scheduleDtos = List.of(new ScheduleDto());

        MultiValueMap<String, String> paramsForm = new LinkedMultiValueMap<>();
        paramsForm.add("groupDescription", "group_1");
        paramsForm.add("teacherId", "");
        paramsForm.add("from", "");
        paramsForm.add("to", "");

        Map<String, String> filters = new HashMap<>();
        filters.put("groupDescription", "group_1");
        filters.put("teacherId", "");
        filters.put("from", "");
        filters.put("to", "");

        when(scheduleService.getScheduleByCriteria(filters)).thenReturn(scheduleDtos);

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/getSchedule")
                .params(paramsForm))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("scheduleDtos", scheduleDtos))
                .andExpect(view().name("scheduleView/schedulesByCriteria"));

        verify(scheduleService).getScheduleByCriteria(filters);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("lessonDate", "2010-05-10");
        form.add("lessonTime", "21:16:00");
        form.add("lectureHallId", "");
        form.add("groupDescription", "");
        form.add("teacherId", "-1");
        form.add("courseName", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("lessonDate", "2040-05-10");
        form.add("lessonTime", "21:16:00");
        form.add("lectureHallId", "2");
        form.add("groupDescription", "any");
        form.add("teacherId", "1");
        form.add("courseName", "any");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("lessonDate", "2010-05-10");
        form.add("lessonTime", "21:16:00");
        form.add("lectureHallId", "");
        form.add("groupDescription", "");
        form.add("teacherId", "-1");
        form.add("courseName", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("lessonDate", "2040-05-10");
        form.add("lessonTime", "21:16:00");
        form.add("lectureHallId", "2");
        form.add("groupDescription", "any");
        form.add("teacherId", "1");
        form.add("courseName", "any");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/schedules/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/schedules/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(scheduleService);
    }
}