package my.project.university.controllers.api;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.TrainingProgramService;
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
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramsRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String COURSE_ID_CONSTRAINT = "Course id should be positive";
    private static final String SPECIALITY_CONSTRAINT = "Training program speciality should not be blank";

    private final TrainingProgramService trainingProgramService;

    @GetMapping("/{id}")
    public HttpEntity<TrainingProgramDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        TrainingProgramDto dto = trainingProgramService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/speciality/{speciality}")
    public HttpEntity<TrainingProgramDto> findByName(@PathVariable("speciality") @NotBlank(message = SPECIALITY_CONSTRAINT) String speciality) {
        TrainingProgramDto dto = trainingProgramService.findBySpeciality(speciality);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public HttpEntity<Page<TrainingProgramDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TrainingProgramDto> page = trainingProgramService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<TrainingProgramDto> add(@RequestBody @Valid TrainingProgramDto dto) {
        TrainingProgramDto added = trainingProgramService.saveOrUpdate(dto);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<TrainingProgramDto> update(@RequestBody @Valid TrainingProgramDto dto) {
        TrainingProgramDto updated = trainingProgramService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id) {
        trainingProgramService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/courses")
    public HttpEntity<Page<CourseDto>> getCourses(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseDto> page = trainingProgramService.getCourses(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{programId}/courses/{courseId}")
    public HttpEntity<Void> addCourse(@PathVariable("programId") @Positive(message = ID_CONSTRAINT) Integer programId,
                                      @PathVariable("courseId") @Positive(message = COURSE_ID_CONSTRAINT) Integer courseId) {

        trainingProgramService.addCourse(programId, courseId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{programId}/courses/{courseId}")
    public HttpEntity<Void> deleteCourse(@PathVariable("programId") @Positive(message = ID_CONSTRAINT) Integer programId,
                                         @PathVariable("courseId") @Positive(message = COURSE_ID_CONSTRAINT) Integer courseId) {

        trainingProgramService.deleteCourse(programId, courseId);
        return ResponseEntity.ok().build();
    }
}
