package my.project.university.controllers.view;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.StudentDto;
import my.project.university.services.interfaces.StudentService;
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
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@Validated
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentsController {

    private static final String ID_CONSTRAINT = "Id should be positive";
    private final StudentService studentService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        StudentDto studentDto = studentService.findById(id);
        model.addAttribute("studentDto", studentDto);
        return "students/student";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<StudentDto> page = studentService.findAll(pageable);
        model.addAttribute("page", page);
        return "students/students";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid StudentDto studentDto) {
        studentService.saveOrUpdate(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        StudentDto studentDto = studentService.findById(id);
        model.addAttribute("updateStudentDto", studentDto);
        return "students/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateStudentDto") @Valid StudentDto studentDto) {
        studentService.saveOrUpdate(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer studentId) {
        studentService.delete(studentId);
        return "redirect:/students";
    }
}
