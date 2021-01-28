package my.project.university.controllers.view;


import lombok.RequiredArgsConstructor;
import my.project.university.mkb.mapping.Person;
import my.project.university.mkb.mapping.PersonDto;
import my.project.university.mkb.mapping.PersonMapper;
import my.project.university.mkb.mapping.PhoneDto;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.property.PropertyClass;
import my.project.university.services.interfaces.CourseService;
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
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CoursesController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String SPECIALITY_CONSTRAINT = "TrainingProgramSpeciality should not be blank";
    private static final String COURSE_NAME_CONSTRAINT = "Course name should not be blank";

    private final CourseService courseService;
    private final PropertyClass propertyClass;
    private final PersonMapper personMapper;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        CourseDto courseDto = courseService.findById(id);
        model.addAttribute("courseDto", courseDto);
        return "courses/course";
    }

    @GetMapping("/byName")
    public String findByName(@RequestParam("name") @NotBlank(message = COURSE_NAME_CONSTRAINT) String name, Model model) {
        CourseDto courseDto = courseService.findByName(name);
        model.addAttribute("courseDto", courseDto);
        return "courses/course";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Model model) {
        Page<CourseDto> page = courseService.findAll(pageable);

        model.addAttribute("page", page);


        PhoneDto phoneDto = new PhoneDto("8-915-308-03-39");

        PersonDto personDto = new PersonDto();
        personDto.setInn("12345");
        personDto.setForAdditional("forAdditional");
        personDto.setForAdditional1("forAdditional1");
        personDto.getPhoneDtos().add(phoneDto);


        Person person = personMapper.defaultMethod(personDto);

        System.out.println(personDto);
        System.out.println(person);


        return "courses/courses";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid CourseDto courseDto) {
        courseService.saveOrUpdate(courseDto);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") @Positive Integer id, Model model) {
        CourseDto courseDto = courseService.findById(id);
        model.addAttribute("updateCourseDto", courseDto);
        return "courses/updateForm";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateCourseDto") @Valid CourseDto courseDto) {
        courseService.saveOrUpdate(courseDto);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id) {
        courseService.delete(id);
        return "redirect:/courses";
    }

    @GetMapping("/{courseId}/teachers")
    public String getTeachers(@PathVariable("courseId") @Positive Integer courseId,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                              Model model) {
        Page<TeacherDto> teachers = courseService.getTeachers(courseId, pageable);
        CourseDto course = courseService.findById(courseId);

        model.addAttribute("teachers", teachers);
        model.addAttribute("course", course);
        return "courses/teachers";

    }

    @PostMapping("/{courseId}/teachers")
    public String addTeacher(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                             @RequestParam("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId) {

        courseService.addTeacher(courseId, teacherId);

        return String.format("redirect:/courses/%d/teachers", courseId);
    }

    @GetMapping("/{courseId}/teachers/{teacherId}/delete")
    public String deleteTeacher(@PathVariable("courseId") Integer courseId,
                                @PathVariable("teacherId") Integer teacherId) {
        courseService.deleteTeacher(courseId, teacherId);
        return String.format("redirect:/courses/%d/teachers", courseId);
    }

    @GetMapping("/{courseId}/training-programs")
    public String getTrainingProgram(@PathVariable("courseId") @Positive Integer courseId,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                     Model model) {
        Page<TrainingProgramDto> trainingPrograms = courseService.getTrainingPrograms(courseId, pageable);
        CourseDto course = courseService.findById(courseId);

        model.addAttribute("programs", trainingPrograms);
        model.addAttribute("course", course);
        return "courses/trainingPrograms";

    }

    @PostMapping("/{courseId}/training-programs")
    public String addTrainingProgram(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                                     @RequestParam("speciality") @NotBlank(message = SPECIALITY_CONSTRAINT) String speciality) {

        courseService.addTrainingProgram(courseId, speciality);

        return String.format("redirect:/courses/%d/training-programs", courseId);
    }

    @GetMapping("/{courseId}/training-programs/{programId}/delete")
    public String deleteTrainingProgram(@PathVariable("courseId") Integer courseId,
                                        @PathVariable("programId") Integer programId) {
        courseService.deleteTrainingProgram(courseId, programId);
        return String.format("redirect:/courses/%d/training-programs", courseId);
    }
}
