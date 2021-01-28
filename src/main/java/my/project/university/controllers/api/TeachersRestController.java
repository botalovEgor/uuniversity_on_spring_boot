package my.project.university.controllers.api;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.services.interfaces.TeacherService;
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
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String COURSE_ID_CONSTRAINT = "Course id should be positive";


    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public HttpEntity<TeacherDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        TeacherDto dto = teacherService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public HttpEntity<Page<TeacherDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TeacherDto> page = teacherService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<TeacherDto> add(@RequestBody @Valid TeacherDto dto) {
        TeacherDto added = teacherService.saveOrUpdate(dto);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<TeacherDto> update(@RequestBody @Valid TeacherDto dto) {
        TeacherDto updated = teacherService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer teacherId) {
        teacherService.delete(teacherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/courses")
    public HttpEntity<Page<CourseDto>> getCourses(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseDto> page = teacherService.getCourses(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{teacherId}/courses/{courseId}")
    public HttpEntity<Void> addCourse(@PathVariable("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId,
                                      @PathVariable("courseId") @Positive(message = COURSE_ID_CONSTRAINT) Integer courseId) {

        teacherService.addCourse(teacherId, courseId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{teacherId}/courses/{courseId}")
    public HttpEntity<Void> deleteCourse(@PathVariable("teacherId") @Positive(message = ID_CONSTRAINT) Integer teacherId,
                                          @PathVariable("courseId") @Positive(message = COURSE_ID_CONSTRAINT) Integer courseId) {

        teacherService.deleteCourse(teacherId, courseId);
        return ResponseEntity.ok().build();
    }
}
