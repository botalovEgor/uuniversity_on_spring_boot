package my.project.university.controllers.view;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.services.interfaces.CourseService;
import my.project.university.services.interfaces.TeacherService;
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
import java.util.List;

@Controller
@Validated
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeachersController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String COURSE_NAME_CONSTRAINT = "Course name should not be blank";
    private static final String COURSE_ID_CONSTRAINT = "Course id should be positive";


    private final TeacherService teacherService;
    private final CourseService courseService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        TeacherDto teacherDto = teacherService.findById(id);
        model.addAttribute("teacherDto", teacherDto);
        return "teachers/teacher";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<TeacherDto> page = teacherService.findAll(pageable);
        model.addAttribute("page", page);
        return "teachers/teachers";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid TeacherDto teacherDto) {
        teacherService.saveOrUpdate(teacherDto);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        TeacherDto teacherDto = teacherService.findById(id);
        model.addAttribute("updateTeacherDto", teacherDto);
        return "teachers/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateTeacherDto") @Valid TeacherDto teacherDto) {
        teacherService.saveOrUpdate(teacherDto);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer teacherId) {
        teacherService.delete(teacherId);
        return "redirect:/teachers";
    }

    @GetMapping("/{teacherId}/courses")
    public String getCourses(@PathVariable("teacherId") @Positive Integer teacherId,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                              Model model) {
        Page<CourseDto> courses = teacherService.getCourses(teacherId, pageable);
        TeacherDto teacher = teacherService.findById(teacherId);

        model.addAttribute("courses", courses);
        model.addAttribute("teacher", teacher);
        return "teachers/courses";

    }

    @PostMapping("/{id}/courses")
    public String addCourse(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer teacherId,
                                     @RequestParam("courseName") @NotBlank(message = COURSE_NAME_CONSTRAINT) String courseName) {
        teacherService.addCourse(teacherId, courseName);
        return String.format("redirect:/teachers/%d/courses", teacherId);
    }

    @GetMapping("/{teacherId}/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId,
                                   @PathVariable("courseId") @Positive(message = COURSE_ID_CONSTRAINT) Integer courseId) {
        teacherService.deleteCourse(teacherId, courseId);
        return String.format("redirect:/teachers/%d/courses", teacherId);
    }

}
