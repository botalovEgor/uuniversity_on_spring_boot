package my.project.university.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.dto.GroupDto;
import my.project.university.models.dto.StudentDto;
import my.project.university.serializers.CustomMappingConfiguration;
import my.project.university.services.interfaces.GroupService;
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

@WebMvcTest(controllers = GroupsRestController.class)
@Import(value = CustomMappingConfiguration.class)
class GroupsRestControllerTest {
    private static final String URL_PATH = "/api/groups/";
    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Test
    void findByIdShouldOnlyCallFindByIdMethodAndReturnGroupDtoWithGivenIdWithStatus200() throws Exception {
        GroupDto groupDto = new GroupDto(1, "any", 1, "program");

        when(groupService.findById(1)).thenReturn(groupDto);

        mockMvc.perform(get(URL_PATH + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(groupDto));

        verify(groupService).findById(1);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void findByDescriptionShouldOnlyCallFindByDescriptionMethodAndReturnGroupDtoWithGivenDescriptionWithStatus200() throws Exception {
        GroupDto groupDto = new GroupDto(1, "any", 1, "program");

        when(groupService.findByDescription("any")).thenReturn(groupDto);

        mockMvc.perform(get(URL_PATH + "description/any")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(groupDto));

        verify(groupService).findByDescription("any");
        verifyNoMoreInteractions(groupService);

    }

    @Test
    void showAllShouldOnlyCallFindAllMethodAndReturnPageWithGroupDtoWithStatus200() throws Exception {
        GroupDto dto = new GroupDto(1, "any", 1, "program");
        Page<GroupDto> page = new PageImpl<>(List.of(dto), pageable, 1);

        when(groupService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(groupService).findAll(pageable);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addShouldOnlyCallSaveOrUpdateMethodAndReturnAddedGroupDtoWithStatus201() throws Exception {
        GroupDto adding = new GroupDto(null, "new", 1, "program");
        GroupDto added = new GroupDto(1, "new", 1, "program");

        when(groupService.saveOrUpdate(adding)).thenReturn(added);

        String requestBody = "{\n" +
                "    \"id\": null,\n" +
                "    \"description\": \"new\",\n" +
                "    \"trainingProgramId\": 1,\n" +
                "    \"trainingProgram\": \"program\"\n" +
                "}";

        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(added));

        verify(groupService).saveOrUpdate(adding);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateShouldOnlyCallSaveOrUpdateMethodAndReturnChangedGroupDtoWithStatus200() throws Exception {
        GroupDto updating = new GroupDto(1, "changed", 1, "program");

        when(groupService.saveOrUpdate(updating)).thenReturn(updating);

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"description\": \"changed\",\n" +
                "    \"trainingProgramId\": 1,\n" +
                "    \"trainingProgram\": \"program\"\n" +
                "}";

        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(updating));

        verify(groupService).saveOrUpdate(updating);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void deleteShouldOnlyCallDeleteMethodAndReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "1"))
                .andExpect(status().isOk());

        verify(groupService).delete(1);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void getStudentsShouldOnlyCallGetTeachersMethodAndReturnPageStudentDtoWithStatus200() throws Exception {
        StudentDto dto = new StudentDto(1, "name", "lastName", "group", 1);
        Page<StudentDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(groupService.getStudents(1, pageable)).thenReturn(page);

        mockMvc.perform(get(URL_PATH + "1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paged").value(true))
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(page.getContent().size()))
                .andExpect(jsonPath("$.content[0]").value(dto));

        verify(groupService).getStudents(1, pageable);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addStudentShouldOnlyCallAddStudentMethodAndReturnStatus201() throws Exception {
        mockMvc.perform(post(URL_PATH + "1/students/2"))
                .andExpect(status().isCreated());

        verify(groupService).addStudent(1, 2);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void whenEntityNotFoundByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NotFoundEntityException.class).when(groupService).findById(4);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"4"))
                .andExpect(status().isNotFound());

        verify(groupService).findById(4);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new GroupDto(null, "", 1, ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void addShouldReturnStatusBadRequestWhenDtoIdIsNotNull() throws Exception {
        mockMvc.perform(post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new GroupDto(1, "any", 1, "program"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoValidationWasFailed() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new GroupDto(1, "", 1, ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void updateShouldReturnStatusBadRequestWhenDtoIdIsNull() throws Exception {
        mockMvc.perform(put(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new GroupDto(null, "group", 1, "program"))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void findByIdShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH+"-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void findByNameShouldConstraintViolationExceptionWhenNameIsBlank() throws Exception {
        mockMvc.perform(get(URL_PATH+"description/{description}", " "))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void deleteShouldReturnStatusBadRequestWhenIdNotPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH+ "-2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }

    @Test
    void getStudentsShouldReturnStatusBadRequestWhenGroupIdNotPositive() throws Exception {
        mockMvc.perform(get(URL_PATH+ "-2" + "/students"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(groupService);
    }

    @Test
    void addStudentShouldReturnStatusBadRequestWhenGroupIdAndOrStudentIdNotPositive() throws Exception {
        mockMvc.perform(post(URL_PATH+ "-2" + "/students/" + "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "2" + "/students/" + "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(URL_PATH+ "-2" + "/students/" + "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(groupService);
    }
}