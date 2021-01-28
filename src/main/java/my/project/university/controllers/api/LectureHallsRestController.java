package my.project.university.controllers.api;

import lombok.RequiredArgsConstructor;
import my.project.university.models.LectureHall;
import my.project.university.services.interfaces.LectureHallService;
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
@RequestMapping("/api/lecture-halls")
@Validated
@RequiredArgsConstructor
public class LectureHallsRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";

    private final LectureHallService lectureHallService;

    @GetMapping("/{id}")
    public HttpEntity<LectureHall> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        LectureHall lectureHall = lectureHallService.findById(id);
        return ResponseEntity.ok(lectureHall);
    }

    @GetMapping
    public HttpEntity<Page<LectureHall>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<LectureHall> page = lectureHallService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<LectureHall> add(@RequestBody @Valid LectureHall lectureHall) {
        LectureHall added = lectureHallService.saveOrUpdate(lectureHall);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<LectureHall> update(@RequestBody @Valid LectureHall lectureHall) {
        LectureHall updated = lectureHallService.saveOrUpdate(lectureHall);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer lectureHallId) {
        lectureHallService.delete(lectureHallId);
        return ResponseEntity.ok().build();
    }
}

