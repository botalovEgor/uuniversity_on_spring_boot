package my.project.university.controllers.view;


import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.GroupDto;
import my.project.university.models.dto.StudentDto;
import my.project.university.services.interfaces.GroupService;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Controller
@Validated
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupsController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String GROUP_DESCRIPTION_CONSTRAINT = "Course name should not be blank";

    private final GroupService groupService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        GroupDto groupDto = groupService.findById(id);
        model.addAttribute("groupDto", groupDto);
        return "groups/group";
    }

    @GetMapping("/byDescription")
    public String findByDescription(@RequestParam("description") @NotBlank(message = GROUP_DESCRIPTION_CONSTRAINT) String description, Model model) {
        GroupDto groupDto = groupService.findByDescription(description);
        model.addAttribute("groupDto", groupDto);
        return "groups/group";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<GroupDto> page = groupService.findAll(pageable);
        model.addAttribute("page", page);
        return "groups/groups";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid GroupDto groupDto) {
        groupService.saveOrUpdate(groupDto);
        return "redirect:/groups";
    }

    @GetMapping("/{groupId}/update")
    public String update(@PathVariable("groupId") Integer id, Model model) {
        GroupDto groupDto = groupService.findById(id);
        model.addAttribute("groupDto", groupDto);
        return "groups/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("groupDto") @Valid GroupDto groupDto) {
        groupService.saveOrUpdate(groupDto);
        return "redirect:/groups";
    }

    @GetMapping("/{groupId}/delete")
    public String delete(@PathVariable("groupId") @Positive(message = ID_CONSTRAINT) Integer groupId) {
        groupService.delete(groupId);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/students")
    public String getStudents(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable("id") Integer id,
                              Model model) {
        Page<StudentDto> students = groupService.getStudents(id, pageable);

        GroupDto group = groupService.findById(id);

        model.addAttribute("group", group);
        model.addAttribute("students", students);

        return "groups/students";
    }

    @PostMapping("/{id}/students")
    public String addStudent(@PathVariable("id") Integer groupId,
                             @RequestParam("studentId") Integer studentId){
        groupService.addStudent(groupId, studentId);

        return String.format("redirect:/groups/%d/students", groupId);
    }

}
