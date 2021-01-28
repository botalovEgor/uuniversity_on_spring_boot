package my.project.university.controllers.api;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.StudentDto;
import my.project.university.services.interfaces.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsRestController {

    private static final String ID_CONSTRAINT = "Id should be positive";
    private final StudentService studentService;

    @GetMapping("/{id}")
    public HttpEntity<StudentDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        StudentDto studentDto = studentService.findById(id);
        return ResponseEntity.ok(studentDto);
    }

    @GetMapping
    public HttpEntity<Page<StudentDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<StudentDto> page = studentService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<StudentDto> add(@RequestBody @Valid StudentDto dto) {
        StudentDto added = studentService.saveOrUpdate(dto);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<StudentDto> update(@RequestBody @Valid StudentDto dto) {
        StudentDto updated = studentService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer studentId) {
        studentService.delete(studentId);
        return ResponseEntity.ok().build();
    }

}
