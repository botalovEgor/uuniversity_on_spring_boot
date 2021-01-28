package my.project.university.controllers.api;

import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.services.interfaces.ScheduleService;
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
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class SchedulesRestController {
    private static final String ID_CONSTRAINT = "Id should be positive";

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public HttpEntity<ScheduleDto> findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id) {
        ScheduleDto scheduleDto = scheduleService.findById(id);
        return ResponseEntity.ok(scheduleDto);
    }

    @GetMapping
    public HttpEntity<Page<ScheduleDto>> showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ScheduleDto> page = scheduleService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public HttpEntity<ScheduleDto> add(@RequestBody @Valid ScheduleDto dto) {
        ScheduleDto added = scheduleService.saveOrUpdate(dto);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    @Validated(OnUpdate.class)
    public HttpEntity<ScheduleDto> update(@RequestBody @Valid ScheduleDto dto) {
        ScheduleDto updated = scheduleService.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id) {
        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/filter")
    public HttpEntity<List<ScheduleDto>> getScheduleByCriteria(@RequestParam Map<String, String> filters) {
        List<ScheduleDto> scheduleDtos = scheduleService.getScheduleByCriteria(filters);
        return ResponseEntity.ok(scheduleDtos);
    }
}