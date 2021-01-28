package my.project.university.controllers.view;

import my.project.university.models.dto.GroupDto;
import my.project.university.services.interfaces.GroupService;
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

@WebMvcTest(controllers = GroupsController.class)
class GroupsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Test
    void findById() throws Exception {
        GroupDto groupDto = new GroupDto();
        groupDto.setDescription("group");

        when(groupService.findById(1)).thenReturn(groupDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("groupDto", groupDto))
                .andExpect(view().name("groupView/oneGroup"));

        verify(groupService).findById(1);
        verifyNoMoreInteractions(groupService);

    }

    @Test
    void findByDescription() throws Exception {
        GroupDto groupDto = new GroupDto();
        groupDto.setDescription("group");

        when(groupService.findByDescription("group")).thenReturn(groupDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/byDescription")
                .param("description", "group"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("groupDto", groupDto))
                .andExpect(view().name("groupView/oneGroup"));

        verify(groupService).findByDescription("group");
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void showAll() throws Exception {
        Page<GroupDto> page = new PageImpl<>(List.of(new GroupDto()));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        when(groupService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("page", page))
                .andExpect(view().name("groupView/allGroups"));

        verify(groupService).findAll(pageable);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addWithPostHttp() throws Exception {
        GroupDto adding = new GroupDto();
        adding.setDescription("group_4");
        adding.setTrainingProgram("trainingProgram_1");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("description", "group_4");
        form.add("trainingProgram", "trainingProgram_1");

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/add")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).saveOrUpdate(adding);
        verifyNoMoreInteractions(groupService);
    }


    @Test
    void updateWithGetHttp() throws Exception {
        GroupDto updating = new GroupDto();
        updating.setId(1);
        updating.setDescription("group");
        updating.setTrainingProgram("program");

        when(groupService.findById(1)).thenReturn(updating);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{id}/update", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(1))
                .andExpect(model().attribute("groupDto", updating))
                .andExpect(view().name("groupView/update"));

        verify(groupService).findById(1);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateWithPostHttp() throws Exception {
        GroupDto updating = new GroupDto();
        updating.setId(3);
        updating.setDescription("changed");
        updating.setTrainingProgram("trainingProgram_1");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "3");
        form.add("description", "changed");
        form.add("trainingProgram", "trainingProgram_1");

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/update")
                .params(form))
                .andExpect(model().size(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).saveOrUpdate(updating);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void delete() throws Exception {
        Integer id = 3;
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).delete(3);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("description", "");
        form.add("trainingProgram", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNotNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "5");
        form.add("description", "any");
        form.add("trainingProgram", "any");

        String error = "- Id should be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/add")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateWithPostHttpShouldBindingExceptionWhenDtoInvalid() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "1");
        form.add("description", "");
        form.add("trainingProgram", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateWithPostHttpShouldConstraintViolationExceptionWhenDtoIdIsNull() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("id", "");
        form.add("description", "any");
        form.add("trainingProgram", "5");

        String error = "- Id should not be null\r\n";

        mockMvc.perform(MockMvcRequestBuilders.post("/groups/update")
                .params(form))
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void findByIdShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{id}", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void findByDescriptionShouldConstraintViolationExceptionWhenDescriptionIsBlank() throws Exception {

        String error = "- Course name should not be blank\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/byDescription")
                .param("description", ""))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    void deleteShouldConstraintViolationExceptionWhenIdNotPositive() throws Exception {

        String error = "- Id should be positive\r\n";

        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{id}/delete", -1))
                .andExpect(status().is4xxClientError())
                .andExpect(model().size(1))
                .andExpect(model().attribute("errorMessage", error))
                .andExpect(view().name("exceptionView/error"));

        verifyNoMoreInteractions(groupService);
    }
}