package my.project.university.controllers.view;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.TrainingProgramService;
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
@RequestMapping("/training-programs")
@RequiredArgsConstructor
public class TrainingProgramsController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String COURSE_NAME_CONSTRAINT = "Course name should not be blank";

    private final TrainingProgramService trainingProgramService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        TrainingProgramDto trainingProgramDto = trainingProgramService.findById(id);
        model.addAttribute("trainingProgramDto", trainingProgramDto);
        return "trainingPrograms/trainingProgram";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<TrainingProgramDto> page = trainingProgramService.findAll(pageable);
        model.addAttribute("page", page);
        return "trainingPrograms/trainingPrograms";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid TrainingProgramDto trainingProgramDto) {
        trainingProgramService.saveOrUpdate(trainingProgramDto);
        return "redirect:/training-programs";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        TrainingProgramDto trainingProgramDto = trainingProgramService.findById(id);
        model.addAttribute("updateTrainingProgramDto", trainingProgramDto);
        return "trainingPrograms/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateTrainingProgramDto") @Valid TrainingProgramDto trainingProgramDto) {
        trainingProgramService.saveOrUpdate(trainingProgramDto);
        return "redirect:/training-programs";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer trainingProgramId) {
        trainingProgramService.delete(trainingProgramId);
        return "redirect:/training-programs";
    }

    @GetMapping("/{id}/courses")
    public String getCourses(@PathVariable("id") @Positive Integer programId,
                              @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                              Model model) {
        Page<CourseDto> courses = trainingProgramService.getCourses(programId, pageable);
        TrainingProgramDto trainingProgram = trainingProgramService.findById(programId);

        model.addAttribute("courses", courses);
        model.addAttribute("trainingProgram", trainingProgram);
        return "trainingPrograms/courses";

    }

    @PostMapping("/{id}/courses")
    public String addCourse(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer programId,
                            @RequestParam("courseName") @NotBlank(message = COURSE_NAME_CONSTRAINT) String courseName) {
        trainingProgramService.addCourse(programId, courseName);
        return String.format("redirect:/training-programs/%d/courses", programId);
    }

    @GetMapping("/{programId}/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable("programId") @Positive(message = ID_CONSTRAINT) Integer programId,
                               @PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId) {
        trainingProgramService.deleteCourse(programId, courseId);
        return String.format("redirect:/training-programs/%d/courses", programId);
    }
}
