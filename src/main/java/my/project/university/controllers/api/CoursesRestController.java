package my.project.university.controllers.api;


import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.CourseService;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String COURSE_NAME_CONSTRAINT = "Course name should not be blank";

    private final CourseService courseService;

    @GetMapping("/{id}")
    public HttpEntity<CourseDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        CourseDto courseDto = courseService.findById(id);
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/name/{name}")
    public HttpEntity<CourseDto> findByName(@PathVariable("name") @NotBlank(message = COURSE_NAME_CONSTRAINT) String name) {
        CourseDto courseDto = courseService.findByName(name);
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping
    public HttpEntity<Page<CourseDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseDto> page = courseService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<CourseDto> add(@RequestBody @Valid CourseDto dto) {
        CourseDto added = courseService.saveOrUpdate(dto);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<CourseDto> update(@RequestBody @Valid CourseDto dto) {
        CourseDto updated = courseService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/teachers")
    public HttpEntity<Page<TeacherDto>> getTeachers(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id,
                                                    @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TeacherDto> page = courseService.getTeachers(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{courseId}/teachers/{teacherId}")
    public HttpEntity<Void> addTeacher(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                                       @PathVariable("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId) {

        courseService.addTeacher(courseId, teacherId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/teachers/{teacherId}")
    public HttpEntity<Void> deleteTeacher(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                                          @PathVariable("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId) {

        courseService.deleteTeacher(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/training-programs")
    public HttpEntity<Page<TrainingProgramDto>> getTrainingPrograms(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id,
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<TrainingProgramDto> trainingPrograms = courseService.getTrainingPrograms(id, pageable);

        return ResponseEntity.ok(trainingPrograms);
    }

    @PostMapping("/{courseId}/training-programs/{programId}")
    public HttpEntity<Void> addTrainingPrograms(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                                                @PathVariable("programId") @Positive(message = ID_CONSTRAINT) Integer programId) {

        courseService.addTrainingProgram(courseId, programId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/training-programs/{programId}")
    public HttpEntity<Void> deleteTrainingProgram(@PathVariable("courseId") @Positive(message = ID_CONSTRAINT) Integer courseId,
                                                  @PathVariable("programId") @Positive(message = ID_CONSTRAINT) Integer programId) {

        courseService.deleteTrainingProgram(courseId, programId);
        return ResponseEntity.ok().build();
    }
}
