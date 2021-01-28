package my.project.university.controllers.api;

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
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupsRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";
    private static final String GROUP_DESCRIPTION_CONSTRAINT = "Course name should not be blank";
    private static final String STUDENT_ID_CONSTRAINT = "Student id should be positive";

    private final GroupService groupService;

    @GetMapping("/{id}")
    public HttpEntity<GroupDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        GroupDto groupDto = groupService.findById(id);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping("description/{description}")
    public HttpEntity<GroupDto> findByDescription(@PathVariable("description") @NotBlank(message = GROUP_DESCRIPTION_CONSTRAINT) String description) {
        GroupDto groupDto = groupService.findByDescription(description);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping
    public HttpEntity<Page<GroupDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GroupDto> page = groupService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<GroupDto> add(@RequestBody @Valid GroupDto dto) {
        GroupDto created = groupService.saveOrUpdate(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public  HttpEntity<GroupDto> update(@RequestBody @Valid GroupDto dto) {
        GroupDto updated = groupService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/students")
    public HttpEntity<Page<StudentDto>> getStudents(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id,
                                                    @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<StudentDto> page = groupService.getStudents(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{groupId}/students/{studentId}")
    public HttpEntity<Void> addStudent(@PathVariable("groupId") @Positive(message = ID_CONSTRAINT) Integer groupId,
                                       @PathVariable("studentId") @Positive(message = STUDENT_ID_CONSTRAINT) Integer studentId){
        groupService.addStudent(groupId, studentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
